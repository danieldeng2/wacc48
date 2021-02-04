package analyser

import analyser.nodes.*
import analyser.nodes.assignment.*
import analyser.nodes.expr.*
import analyser.nodes.expr.operators.*
import analyser.nodes.function.*
import analyser.nodes.statement.*
import analyser.nodes.type.*
import antlr.WACCParser
import antlr.WACCParserVisitor
import org.antlr.v4.runtime.tree.*

class ASTGenerator : AbstractParseTreeVisitor<ASTNode>(), WACCParserVisitor<ASTNode> {

    override fun visitProg(ctx: WACCParser.ProgContext): ASTNode =
        ProgNode(
            body = visit(ctx.stat()) as StatNode,
            functions = ctx.func().map { visit(it) as FuncNode }
        )

    override fun visitFunc(ctx: WACCParser.FuncContext): ASTNode =
        FuncNode(
            identifier = ctx.IDENT().text,
            paramList = when {
                ctx.paramList() == null -> ParamListNode(emptyList())
                else -> visit(ctx.paramList()) as ParamListNode
            },
            retType = visit(ctx.type()) as Type,
            body = visit(ctx.stat()) as StatNode
        )

    override fun visitParamList(ctx: WACCParser.ParamListContext): ASTNode =
        ParamListNode(
            params = ctx.param().map { visit(it) as ParamNode }
        )

    override fun visitParam(ctx: WACCParser.ParamContext): ASTNode =
        ParamNode(
            type = visit(ctx.type()) as Type,
            text = ctx.IDENT().text
        )

    override fun visitIfStat(ctx: WACCParser.IfStatContext): ASTNode =
        IfNode(
            proposition = visit(ctx.expr()) as ExprNode,
            trueStat = visit(ctx.stat(0)) as StatNode,
            falseStat = visit(ctx.stat(1)) as StatNode,
        )

    override fun visitReadStat(ctx: WACCParser.ReadStatContext): ASTNode =
        ReadNode(
            value = visit(ctx.expr()) as ExprNode,
        )

    override fun visitPrintStat(ctx: WACCParser.PrintStatContext): ASTNode =
        PrintNode(
            value = visit(ctx.expr()) as ExprNode,
        )

    override fun visitSeqCompositionStat(ctx: WACCParser.SeqCompositionStatContext): ASTNode =
        SeqNode(
            firstStat = visit(ctx.stat(0)) as StatNode,
            secondStat = visit(ctx.stat(1)) as StatNode,
        )

    override fun visitAssignmentStat(ctx: WACCParser.AssignmentStatContext): ASTNode =
        AssignmentNode(
            name = visit(ctx.assignLhs()) as LHSNode,
            value = visit(ctx.assignRhs()) as RHSNode,
        )

    override fun visitPrintlnStat(ctx: WACCParser.PrintlnStatContext): ASTNode =
        PrintNode(
            value = visit(ctx.expr()) as ExprNode,
            returnAfterPrint = true,
        )

    override fun visitExitStat(ctx: WACCParser.ExitStatContext): ASTNode =
        ExitNode(
            value = visit(ctx.expr()) as ExprNode
        )

    override fun visitFreeStat(ctx: WACCParser.FreeStatContext): ASTNode =
        FreeNode(
            value = visit(ctx.expr()) as ExprNode
        )

    override fun visitBeginStat(ctx: WACCParser.BeginStatContext): ASTNode =
        BeginNode(
            stat = visit(ctx.stat()) as StatNode,
        )

    override fun visitSkipStat(ctx: WACCParser.SkipStatContext): ASTNode =
        SkipNode

    override fun visitReturnStat(ctx: WACCParser.ReturnStatContext): ASTNode =
        ReturnNode(
            value = visit(ctx.expr()) as ExprNode,
        )

    override fun visitDeclarationStat(ctx: WACCParser.DeclarationStatContext): ASTNode =
        DeclarationNode(
            name = visit(ctx.param()) as ParamNode,
            value = visit(ctx.assignRhs()) as RHSNode
        )

    override fun visitWhileStat(ctx: WACCParser.WhileStatContext): ASTNode =
        WhileNode(
            proposition = visit(ctx.expr()) as ExprNode,
            body = visit(ctx.stat()) as StatNode,
        )

    override fun visitAssignLhs(ctx: WACCParser.AssignLhsContext): ASTNode =
        when {
            ctx.arrayElem() != null -> visit(ctx.arrayElem())
            ctx.pairElem() != null -> visit(ctx.pairElem())
            else -> IdentifierNode(
                name = ctx.text
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

    override fun visitArgList(ctx: WACCParser.ArgListContext): ASTNode =
        ArgListNode(
            args = ctx.expr().map { visit(it) as ExprNode }
        )

    override fun visitType(ctx: WACCParser.TypeContext): ASTNode =
        when {
            ctx.type() != null ->
                ArrayType(
                    elementType = visit(ctx.type()) as Type
                )
            ctx.baseType() != null -> visit(ctx.baseType())
            else -> visit(ctx.pairType())
        }

    override fun visitBaseType(ctx: WACCParser.BaseTypeContext): ASTNode =
        when {
            ctx.BOOL() != null -> BoolType
            ctx.CHAR() != null -> CharType
            ctx.STRING() != null -> StringType
            else -> IntType
        }

    override fun visitPairType(ctx: WACCParser.PairTypeContext): ASTNode =
        PairType(
            firstType = visit(ctx.pairElemType(0)) as Type,
            secondType = visit(ctx.pairElemType(1)) as Type
        )

    override fun visitPairElemType(ctx: WACCParser.PairElemTypeContext): ASTNode =
        when {
            ctx.baseType() != null -> visit(ctx.baseType())
            ctx.type() != null -> visit(ctx.type())
            else -> EmptyPair
        }

    override fun visitStrLiteral(ctx: WACCParser.StrLiteralContext): ASTNode =
        StringLiteral(
            value = ctx.STR_LITER().text
        )

    override fun visitIdentifier(ctx: WACCParser.IdentifierContext): ASTNode =
        IdentifierNode(
            name = ctx.text
        )

    override fun visitUnaryOpExpr(ctx: WACCParser.UnaryOpExprContext): ASTNode =
        UnOpNode(
            operator = UnaryOperator.lookupRepresentation(
                ctx.UNARY_OPERATOR().text
            )!!,
            expr = visit(ctx.expr()) as ExprNode
        )

    override fun visitBinOpExpr(ctx: WACCParser.BinOpExprContext): ASTNode =
        BinOpNode(
            operator = BinaryOperator.lookupRepresentation(
                ctx.BINARY_OPERATOR().text
            )!!,
            firstExpr = visit(ctx.expr(0)) as ExprNode,
            secondExpr = visit(ctx.expr(1)) as ExprNode
        )

    override fun visitPairLiteral(ctx: WACCParser.PairLiteralContext): ASTNode =
        PairLiteral

    override fun visitIntLiteral(ctx: WACCParser.IntLiteralContext): ASTNode =
        IntLiteral(
            value = ctx.text.toInt()
        )

    override fun visitCharLiteral(ctx: WACCParser.CharLiteralContext): ASTNode =
        CharLiteral(
            value = ctx.text[0]
        )

    override fun visitArrayElemExpr(ctx: WACCParser.ArrayElemExprContext): ASTNode =
        visit(ctx.arrayElem())

    override fun visitBoolLiteral(ctx: WACCParser.BoolLiteralContext): ASTNode =
        BoolLiteral(
            value = ctx.text == "true"
        )

    override fun visitBracketedExpr(ctx: WACCParser.BracketedExprContext): ASTNode =
        visit(ctx.expr())

    override fun visitPairElem(ctx: WACCParser.PairElemContext): ASTNode =
        PairElemNode(
            name = visit(ctx.expr()) as ExprNode,
            isFirst = ctx.FST() != null
        )

    override fun visitArrayLiter(ctx: WACCParser.ArrayLiterContext): ASTNode =
        ArrayLiteral(
            values = ctx.expr().map { visit(it) as ExprNode }
        )

    override fun visitArrayElem(ctx: WACCParser.ArrayElemContext): ASTNode =
        ArrayElement(
            name = ctx.IDENT().text,
            indices = ctx.expr().map { visit(it) as ExprNode }
        )

    override fun visitFuncCall(ctx: WACCParser.FuncCallContext): ASTNode =
        FuncCallNode(
            name = ctx.IDENT().text,
            argList = visit(ctx.argList()) as ArgListNode,
        )

    override fun visitNewPair(ctx: WACCParser.NewPairContext): ASTNode =
        NewPairNode(
            firstElem = visit(ctx.expr(0)) as ExprNode,
            secondElem = visit(ctx.expr(1)) as ExprNode,
        )
}