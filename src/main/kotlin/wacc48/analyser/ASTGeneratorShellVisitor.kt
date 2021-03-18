package wacc48.analyser

import org.apache.commons.text.StringEscapeUtils
import wacc48.antlr.WACCShellParser
import wacc48.antlr.WACCShellParserBaseVisitor
import wacc48.tree.nodes.ASTNode
import wacc48.tree.nodes.assignment.AssignmentNode
import wacc48.tree.nodes.assignment.LHSNode
import wacc48.tree.nodes.assignment.NewPairNode
import wacc48.tree.nodes.assignment.PairElemNode
import wacc48.tree.nodes.assignment.RHSNode
import wacc48.tree.nodes.expr.ArrayElement
import wacc48.tree.nodes.expr.ArrayLiteral
import wacc48.tree.nodes.expr.BoolLiteral
import wacc48.tree.nodes.expr.CharLiteral
import wacc48.tree.nodes.expr.ExprNode
import wacc48.tree.nodes.expr.IdentifierNode
import wacc48.tree.nodes.expr.IntLiteral
import wacc48.tree.nodes.expr.PairLiteral
import wacc48.tree.nodes.expr.StringLiteral
import wacc48.tree.nodes.expr.operators.BinOpNode
import wacc48.tree.nodes.expr.operators.UnOpNode
import wacc48.tree.nodes.expr.operators.operation.lookupBinary
import wacc48.tree.nodes.expr.operators.operation.lookupUnary
import wacc48.tree.nodes.function.ArgListNode
import wacc48.tree.nodes.function.FuncCallNode
import wacc48.tree.nodes.function.FuncNode
import wacc48.tree.nodes.function.ParamNode
import wacc48.tree.nodes.statement.BeginNode
import wacc48.tree.nodes.statement.DeclarationNode
import wacc48.tree.nodes.statement.ExitNode
import wacc48.tree.nodes.statement.FreeNode
import wacc48.tree.nodes.statement.IfNode
import wacc48.tree.nodes.statement.PrintNode
import wacc48.tree.nodes.statement.ReadNode
import wacc48.tree.nodes.statement.ReturnNode
import wacc48.tree.nodes.statement.SeqNode
import wacc48.tree.nodes.statement.SkipNode
import wacc48.tree.nodes.statement.StatNode
import wacc48.tree.nodes.statement.WhileNode
import wacc48.tree.type.ArrayType
import wacc48.tree.type.BoolType
import wacc48.tree.type.CharType
import wacc48.tree.type.EmptyPair
import wacc48.tree.type.IntType
import wacc48.tree.type.PairType
import wacc48.tree.type.StringType
import wacc48.tree.type.Type

class ASTGeneratorShellVisitor : WACCShellParserBaseVisitor<ASTNode>() {
    var inFuncNodeCtx = false

    override fun visitCommand(ctx: WACCShellParser.CommandContext): ASTNode =
        when {
            ctx.func() != null -> visit(ctx.func()) as FuncNode
            ctx.stat() != null -> visit(ctx.stat()) as StatNode
            else -> visit(ctx.expr()) as ExprNode
        }

    override fun visitFunc(ctx: WACCShellParser.FuncContext): ASTNode {
        inFuncNodeCtx = true

        val func = FuncNode(
            identifier = ctx.IDENT().text,
            paramList = ctx.paramList()?.param()?.map { visit(it) as ParamNode }
                ?: emptyList(),
            retType = findType(ctx.type()),
            body = visit(ctx.stat()) as StatNode,
            ctx = ctx
        )

        inFuncNodeCtx = false

        return func
    }

    override fun visitFuncCall(ctx: WACCShellParser.FuncCallContext): ASTNode =
        FuncCallNode(
            name = ctx.IDENT().text,
            argList = visitArgList(ctx.argList()) as ArgListNode,
            ctx = ctx,
            inShellAndFuncNodeCtx = inFuncNodeCtx
        )

    override fun visitParam(ctx: WACCShellParser.ParamContext): ASTNode =
        ParamNode(
            type = findType(ctx.type()),
            text = ctx.IDENT().text,
            ctx = ctx
        )

    override fun visitIfStat(ctx: WACCShellParser.IfStatContext): ASTNode =
        IfNode(
            proposition = visit(ctx.expr()) as ExprNode,
            trueStat = visit(ctx.stat(0)) as StatNode,
            falseStat = visit(ctx.stat(1)) as StatNode,
            ctx = ctx
        )

    override fun visitReadStat(ctx: WACCShellParser.ReadStatContext): ASTNode =
        ReadNode(
            value = visit(ctx.assignLhs()) as LHSNode,
            ctx = ctx
        )

    override fun visitPrintStat(ctx: WACCShellParser.PrintStatContext): ASTNode =
        PrintNode(
            value = visit(ctx.expr()) as ExprNode,
            ctx = ctx
        )

    override fun visitSeqCompositionStat(ctx: WACCShellParser.SeqCompositionStatContext): ASTNode =
        SeqNode(
            sequence =
            mutableListOf<StatNode>()
                .apply {
                    var next = ctx as WACCShellParser.StatContext
                    while (next is WACCShellParser.SeqCompositionStatContext) {
                        add(visit(next.stat(0)) as StatNode)
                        next = next.stat(1)
                    }
                    add(visit(next) as StatNode)
                },
            ctx = ctx
        )


    override fun visitAssignmentStat(ctx: WACCShellParser.AssignmentStatContext): ASTNode =
        AssignmentNode(
            name = visit(ctx.assignLhs()) as LHSNode,
            value = visit(ctx.assignRhs()) as RHSNode,
            ctx = ctx
        )

    override fun visitPrintlnStat(ctx: WACCShellParser.PrintlnStatContext): ASTNode =
        PrintNode(
            value = visit(ctx.expr()) as ExprNode,
            returnAfterPrint = true,
            ctx = ctx
        )

    override fun visitExitStat(ctx: WACCShellParser.ExitStatContext): ASTNode =
        ExitNode(
            expr = visit(ctx.expr()) as ExprNode,
            ctx = ctx
        )

    override fun visitFreeStat(ctx: WACCShellParser.FreeStatContext): ASTNode =
        FreeNode(
            value = visit(ctx.expr()) as ExprNode,
            ctx = ctx
        )

    override fun visitBeginStat(ctx: WACCShellParser.BeginStatContext): ASTNode =
        BeginNode(
            stat = visit(ctx.stat()) as StatNode,
            ctx = ctx
        )

    override fun visitSkipStat(ctx: WACCShellParser.SkipStatContext): ASTNode =
        SkipNode

    override fun visitReturnStat(ctx: WACCShellParser.ReturnStatContext): ASTNode =
        ReturnNode(
            value = visit(ctx.expr()) as ExprNode,
            ctx = ctx
        )

    override fun visitDeclarationStat(ctx: WACCShellParser.DeclarationStatContext): ASTNode =
        DeclarationNode(
            name = visit(ctx.param()) as ParamNode,
            value = visit(ctx.assignRhs()) as RHSNode,
            ctx = ctx
        )

    override fun visitWhileStat(ctx: WACCShellParser.WhileStatContext): ASTNode =
        WhileNode(
            proposition = visit(ctx.expr()) as ExprNode,
            body = visit(ctx.stat()) as StatNode,
            ctx = ctx
        )

    override fun visitAssignLhs(ctx: WACCShellParser.AssignLhsContext): ASTNode =
        when {
            ctx.arrayElem() != null -> visit(ctx.arrayElem())
            ctx.pairElem() != null -> visit(ctx.pairElem())
            else -> IdentifierNode(
                name = ctx.text,
                ctx = ctx
            )
        }


    override fun visitAssignRhs(ctx: WACCShellParser.AssignRhsContext): ASTNode =
        when {
            ctx.expr() != null -> visit(ctx.expr())
            ctx.arrayLiter() != null -> visit(ctx.arrayLiter())
            ctx.newPair() != null -> visit(ctx.newPair())
            ctx.pairElem() != null -> visit(ctx.pairElem())
            else -> visit(ctx.funcCall())
        }

    override fun visitArgList(ctx: WACCShellParser.ArgListContext?): ASTNode =
        ArgListNode(
            args = ctx?.expr()?.map { visit(it) as ExprNode }
                ?: emptyList(),
            ctx = ctx
        )

    override fun visitStrLiteral(ctx: WACCShellParser.StrLiteralContext): ASTNode =
        StringLiteral(
            value = ctx.STR_LITER().text.substring(
                1,
                ctx.STR_LITER().text.length - 1
            ),
            ctx = ctx
        )

    override fun visitIdentifier(ctx: WACCShellParser.IdentifierContext): ASTNode =
        IdentifierNode(
            name = ctx.text,
            ctx = ctx
        )

    override fun visitUnaryOpExpr(ctx: WACCShellParser.UnaryOpExprContext): ASTNode =
        visit(ctx.unaryOperator()) as UnOpNode

    override fun visitUnaryOperator(ctx: WACCShellParser.UnaryOperatorContext): ASTNode =
        UnOpNode(
            operation = lookupUnary(ctx.getChild(0).text)!!,
            expr = visit(
                (ctx.getParent()
                    .ruleContext as WACCShellParser.UnaryOpExprContext).expr()
            ) as ExprNode,
            ctx = ctx
        )

    override fun visitBinOpExpr(ctx: WACCShellParser.BinOpExprContext): ASTNode =
        BinOpNode(
            operation = lookupBinary(ctx.op.text)!!,
            firstExpr = visit(ctx.expr(0)) as ExprNode,
            secondExpr = visit(ctx.expr(1)) as ExprNode,
            ctx = ctx
        )


    override fun visitPairLiteral(ctx: WACCShellParser.PairLiteralContext): ASTNode =
        PairLiteral


    override fun visitIntLiteral(ctx: WACCShellParser.IntLiteralContext): ASTNode {
        var isOutOfBounds = false

        val parsedInt = try {
            ctx.text.toInt()
        } catch (e: NumberFormatException) {
            isOutOfBounds = true
            0
        }

        return IntLiteral(
            value = parsedInt,
            isOutOfBounds = isOutOfBounds,
            ctx = ctx
        )
    }

    override fun visitCharLiteral(ctx: WACCShellParser.CharLiteralContext): ASTNode =
        CharLiteral(
            value = when (ctx.text.substring(1, ctx.text.length - 1)) {
                "\\0" -> '\u0000'
                else -> StringEscapeUtils.unescapeJava(ctx.text)[1]
            },
            ctx = ctx
        )

    override fun visitArrayElemExpr(ctx: WACCShellParser.ArrayElemExprContext): ASTNode =
        visit(ctx.arrayElem())

    override fun visitBoolLiteral(ctx: WACCShellParser.BoolLiteralContext): ASTNode =
        BoolLiteral(
            value = ctx.text == "true",
            ctx = ctx
        )

    override fun visitBracketedExpr(ctx: WACCShellParser.BracketedExprContext): ASTNode =
        visit(ctx.expr())

    override fun visitPairElem(ctx: WACCShellParser.PairElemContext): ASTNode =
        PairElemNode(
            expr = visit(ctx.expr()) as ExprNode,
            isFirst = ctx.FST() != null,
            ctx = ctx
        )

    override fun visitArrayLiter(ctx: WACCShellParser.ArrayLiterContext): ASTNode =
        ArrayLiteral(
            values = ctx.expr().map { visit(it) as ExprNode },
            ctx = ctx,
            nameInMemTable =
            if (ctx.getParent() is WACCShellParser.DeclarationStatContext)
                (ctx.getParent() as WACCShellParser.DeclarationStatContext).param().IDENT().text
            else
                null
        )

    override fun visitArrayElem(ctx: WACCShellParser.ArrayElemContext): ASTNode =
        ArrayElement(
            name = ctx.IDENT().text,
            arrIndices = ctx.expr().map { visit(it) as ExprNode },
            ctx = ctx
        )


    override fun visitNewPair(ctx: WACCShellParser.NewPairContext): ASTNode =
        NewPairNode(
            firstElem = visit(ctx.expr(0)) as ExprNode,
            secondElem = visit(ctx.expr(1)) as ExprNode,
            ctx = ctx
        )

    private fun findType(ctx: WACCShellParser.TypeContext): Type =
        when {
            ctx.type() != null -> ArrayType(
                elementType = findType(ctx.type())
            )
            ctx.baseType() != null -> findBaseType(ctx.baseType())
            else -> PairType(
                firstType = findPairElemType(ctx.pairType().pairElemType(0)),
                secondType = findPairElemType(ctx.pairType().pairElemType(1)),
                ctx = ctx
            )
        }

    private fun findBaseType(ctx: WACCShellParser.BaseTypeContext): Type =
        when {
            ctx.BOOL() != null -> BoolType
            ctx.CHAR() != null -> CharType
            ctx.STRING() != null -> StringType
            else -> IntType
        }

    private fun findPairElemType(ctx: WACCShellParser.PairElemTypeContext): Type =
        when {
            ctx.type() != null -> ArrayType(
                elementType = findType(ctx.type())
            )
            ctx.baseType() != null -> findBaseType(ctx.baseType())
            else -> EmptyPair
        }

}