package wacc48.analyser

import WACCParser
import WACCParserBaseVisitor
import wacc48.analyser.exceptions.SyntaxException
import org.apache.commons.text.StringEscapeUtils
import wacc48.tree.nodes.ASTNode
import wacc48.tree.nodes.ProgNode
import wacc48.tree.nodes.assignment.*
import wacc48.tree.nodes.expr.*
import wacc48.tree.nodes.expr.operators.BinOpNode
import wacc48.tree.nodes.expr.operators.BinaryOperator
import wacc48.tree.nodes.expr.operators.UnOpNode
import wacc48.tree.nodes.expr.operators.UnaryOperator
import wacc48.tree.nodes.function.*
import wacc48.tree.nodes.statement.*
import wacc48.tree.type.*


class ASTGeneratorVisitor : WACCParserBaseVisitor<ASTNode>() {
    override fun visitProg(ctx: WACCParser.ProgContext): ASTNode {
        val functions = ctx.func().map { visit(it) as FuncNode }.toMutableList()

        return ProgNode(
            functions = functions,
            main = MainNode(
                body = visit(ctx.stat()) as StatNode,
                ctx = ctx
            ),
            ctx = ctx
        )
    }

    override fun visitFunc(ctx: WACCParser.FuncContext): ASTNode =
        FuncNode(
            identifier = ctx.IDENT().text,
            paramList = ctx.paramList()?.param()?.map { visit(it) as ParamNode }
                ?: emptyList(),
            retType = findType(ctx.type()),
            body = visit(ctx.stat()) as StatNode,
            ctx = ctx
        )

    override fun visitParam(ctx: WACCParser.ParamContext): ASTNode =
        ParamNode(
            type = findType(ctx.type()),
            text = ctx.IDENT().text,
            ctx = ctx
        )

    override fun visitIfStat(ctx: WACCParser.IfStatContext): ASTNode =
        IfNode(
            proposition = visit(ctx.expr()) as ExprNode,
            trueStat = visit(ctx.stat(0)) as StatNode,
            falseStat = visit(ctx.stat(1)) as StatNode,
            ctx = ctx
        )

    override fun visitReadStat(ctx: WACCParser.ReadStatContext): ASTNode =
        ReadNode(
            value = visit(ctx.assignLhs()) as LHSNode,
            ctx = ctx
        )

    override fun visitPrintStat(ctx: WACCParser.PrintStatContext): ASTNode =
        PrintNode(
            value = visit(ctx.expr()) as ExprNode,
            ctx = ctx
        )

    override fun visitSeqCompositionStat(ctx: WACCParser.SeqCompositionStatContext): ASTNode =
        SeqNode(
            sequence =
            mutableListOf<StatNode>()
                .apply {
                    var next = ctx as WACCParser.StatContext
                    while (next is WACCParser.SeqCompositionStatContext) {
                        add(visit(next.stat(0)) as StatNode)
                        next = next.stat(1)
                    }
                    add(visit(next) as StatNode)
                },
            ctx = ctx
        )


    override fun visitAssignmentStat(ctx: WACCParser.AssignmentStatContext): ASTNode =
        AssignmentNode(
            name = visit(ctx.assignLhs()) as LHSNode,
            value = visit(ctx.assignRhs()) as RHSNode,
            ctx = ctx
        )

    override fun visitPrintlnStat(ctx: WACCParser.PrintlnStatContext): ASTNode =
        PrintNode(
            value = visit(ctx.expr()) as ExprNode,
            returnAfterPrint = true,
            ctx = ctx
        )

    override fun visitExitStat(ctx: WACCParser.ExitStatContext): ASTNode =
        ExitNode(
            expr = visit(ctx.expr()) as ExprNode,
            ctx = ctx
        )

    override fun visitFreeStat(ctx: WACCParser.FreeStatContext): ASTNode =
        FreeNode(
            value = visit(ctx.expr()) as ExprNode,
            ctx = ctx
        )

    override fun visitBeginStat(ctx: WACCParser.BeginStatContext): ASTNode =
        BeginNode(
            stat = visit(ctx.stat()) as StatNode,
            ctx = ctx
        )

    override fun visitSkipStat(ctx: WACCParser.SkipStatContext): ASTNode =
        SkipNode

    override fun visitReturnStat(ctx: WACCParser.ReturnStatContext): ASTNode =
        ReturnNode(
            value = visit(ctx.expr()) as ExprNode,
            ctx = ctx
        )

    override fun visitDeclarationStat(ctx: WACCParser.DeclarationStatContext): ASTNode =
        DeclarationNode(
            name = visit(ctx.param()) as ParamNode,
            value = visit(ctx.assignRhs()) as RHSNode,
            ctx = ctx
        )

    override fun visitWhileStat(ctx: WACCParser.WhileStatContext): ASTNode =
        WhileNode(
            proposition = visit(ctx.expr()) as ExprNode,
            body = visit(ctx.stat()) as StatNode,
            ctx = ctx
        )

    override fun visitAssignLhs(ctx: WACCParser.AssignLhsContext): ASTNode =
        when {
            ctx.arrayElem() != null -> visit(ctx.arrayElem())
            ctx.pairElem() != null -> visit(ctx.pairElem())
            else -> IdentifierNode(
                name = ctx.text,
                ctx = ctx
            )
        }


    override fun visitAssignRhs(ctx: WACCParser.AssignRhsContext): ASTNode =
        when {
            ctx.expr() != null -> visit(ctx.expr())
            ctx.arrayLiter() != null -> visit(ctx.arrayLiter())
            ctx.newPair() != null -> visit(ctx.newPair())
            ctx.pairElem() != null -> visit(ctx.pairElem())
            else -> visit(ctx.funcCall())
        }

    override fun visitArgList(ctx: WACCParser.ArgListContext?): ASTNode =
        ArgListNode(
            args = ctx?.expr()?.map { visit(it) as ExprNode }
                ?: emptyList(),
            ctx = ctx
        )

    override fun visitStrLiteral(ctx: WACCParser.StrLiteralContext): ASTNode =
        StringLiteral(
            value = ctx.STR_LITER().text.substring(
                1,
                ctx.STR_LITER().text.length - 1
            ),
            ctx = ctx
        )

    override fun visitIdentifier(ctx: WACCParser.IdentifierContext): ASTNode =
        IdentifierNode(
            name = ctx.text,
            ctx = ctx
        )

    override fun visitUnaryOpExpr(ctx: WACCParser.UnaryOpExprContext): ASTNode =
        visit(ctx.unaryOperator()) as UnOpNode

    override fun visitUnaryOperator(ctx: WACCParser.UnaryOperatorContext): ASTNode =
        UnOpNode(
            operator = UnaryOperator.lookupRepresentation(
                ctx.getChild(0).text
            )!!,
            expr = visit(
                (ctx.getParent()
                    .ruleContext as WACCParser.UnaryOpExprContext).expr()
            ) as ExprNode,
            ctx = ctx
        )

    override fun visitBinOpExpr(ctx: WACCParser.BinOpExprContext): ASTNode =
        BinOpNode(
            operator = BinaryOperator.lookupRepresentation(
                ctx.op.text
            )!!,
            firstExpr = visit(ctx.expr(0)) as ExprNode,
            secondExpr = visit(ctx.expr(1)) as ExprNode,
            ctx = ctx
        )


    override fun visitPairLiteral(ctx: WACCParser.PairLiteralContext): ASTNode =
        PairLiteral


    override fun visitIntLiteral(ctx: WACCParser.IntLiteralContext): ASTNode =
        IntLiteral(
            value = try {
                ctx.text.toInt()
            } catch (e: NumberFormatException) {
                throw SyntaxException("Integer ${ctx.text} out of bounds")
            },
            ctx = ctx
        )

    override fun visitCharLiteral(ctx: WACCParser.CharLiteralContext): ASTNode =
        CharLiteral(
            value = when (ctx.text.substring(1, ctx.text.length - 1)) {
                "\\0" -> '\u0000'
                else -> StringEscapeUtils.unescapeJava(ctx.text)[1]
            },
            ctx = ctx
        )

    override fun visitArrayElemExpr(ctx: WACCParser.ArrayElemExprContext): ASTNode =
        visit(ctx.arrayElem())

    override fun visitBoolLiteral(ctx: WACCParser.BoolLiteralContext): ASTNode =
        BoolLiteral(
            value = ctx.text == "true",
            ctx = ctx
        )

    override fun visitBracketedExpr(ctx: WACCParser.BracketedExprContext): ASTNode =
        visit(ctx.expr())

    override fun visitPairElem(ctx: WACCParser.PairElemContext): ASTNode =
        PairElemNode(
            expr = visit(ctx.expr()) as ExprNode,
            isFirst = ctx.FST() != null,
            ctx = ctx
        )

    override fun visitArrayLiter(ctx: WACCParser.ArrayLiterContext): ASTNode =
        ArrayLiteral(
            values = ctx.expr().map { visit(it) as ExprNode },
            ctx = ctx
        )

    override fun visitArrayElem(ctx: WACCParser.ArrayElemContext): ASTNode =
        ArrayElement(
            name = ctx.IDENT().text,
            arrIndices = ctx.expr().map { visit(it) as ExprNode },
            ctx = ctx
        )

    override fun visitFuncCall(ctx: WACCParser.FuncCallContext): ASTNode =
        FuncCallNode(
            name = ctx.IDENT().text,
            argList = visitArgList(ctx.argList()) as ArgListNode,
            ctx = ctx
        )

    override fun visitNewPair(ctx: WACCParser.NewPairContext): ASTNode =
        NewPairNode(
            firstElem = visit(ctx.expr(0)) as ExprNode,
            secondElem = visit(ctx.expr(1)) as ExprNode,
            ctx = ctx
        )

    private fun findType(ctx: WACCParser.TypeContext): Type =
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

    private fun findBaseType(ctx: WACCParser.BaseTypeContext): Type =
        when {
            ctx.BOOL() != null -> BoolType
            ctx.CHAR() != null -> CharType
            ctx.STRING() != null -> StringType
            else -> IntType
        }

    private fun findPairElemType(ctx: WACCParser.PairElemTypeContext): Type =
        when {
            ctx.type() != null -> ArrayType(
                elementType = findType(ctx.type()),
                ctx = ctx
            )
            ctx.baseType() != null -> findBaseType(ctx.baseType())
            else -> EmptyPair
        }
}
