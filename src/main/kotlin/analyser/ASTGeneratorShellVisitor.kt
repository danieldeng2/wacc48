package analyser

import WACCShellParserBaseVisitor
import analyser.exceptions.SyntaxException
import org.apache.commons.text.StringEscapeUtils
import tree.nodes.ASTNode
import tree.nodes.assignment.*
import tree.nodes.expr.*
import tree.nodes.expr.operators.BinOpNode
import tree.nodes.expr.operators.BinaryOperator
import tree.nodes.expr.operators.UnOpNode
import tree.nodes.expr.operators.UnaryOperator
import tree.nodes.function.ArgListNode
import tree.nodes.function.FuncCallNode
import tree.nodes.function.FuncNode
import tree.nodes.function.ParamNode
import tree.nodes.statement.*
import tree.type.*

class ASTGeneratorShellVisitor : WACCShellParserBaseVisitor<ASTNode>() {
    var inFuncNodeCtx = false
    //TODO(Get rid of the duplication between ASTGeneratorVisitor and ASTGeneratorShellVisitor)

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
            inFuncNodeCtx = inFuncNodeCtx
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
            operator = UnaryOperator.lookupRepresentation(
                ctx.getChild(0).text
            )!!,
            expr = visit(
                (ctx.getParent()
                    .ruleContext as WACCShellParser.UnaryOpExprContext).expr()
            ) as ExprNode,
            ctx = ctx
        )

    override fun visitBinOpExpr(ctx: WACCShellParser.BinOpExprContext): ASTNode =
        BinOpNode(
            operator = BinaryOperator.lookupRepresentation(
                ctx.op.text
            )!!,
            firstExpr = visit(ctx.expr(0)) as ExprNode,
            secondExpr = visit(ctx.expr(1)) as ExprNode,
            ctx = ctx
        )


    override fun visitPairLiteral(ctx: WACCShellParser.PairLiteralContext): ASTNode =
        PairLiteral


    override fun visitIntLiteral(ctx: WACCShellParser.IntLiteralContext): ASTNode =
        IntLiteral(
            value = try {
                ctx.text.toInt()
            } catch (e: NumberFormatException) {
                throw SyntaxException("Integer ${ctx.text} out of bounds")
            },
            ctx = ctx
        )

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
            ctx = ctx
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
                elementType = findType(ctx.type()),
                ctx = ctx
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
                elementType = findType(ctx.type()),
                ctx = ctx
            )
            ctx.baseType() != null -> findBaseType(ctx.baseType())
            else -> EmptyPair
        }

}