import analyser.nodes.*
import analyser.nodes.assignment.*
import analyser.nodes.expr.*
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
            params = ctx.param().withIndex()
                .map { (_, paramCtx) ->
                    visit(paramCtx) as ParamNode
                }
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
            secondStat = visit(ctx.stat(2)) as StatNode,
        )

    override fun visitAssignmentStat(ctx: WACCParser.AssignmentStatContext): ASTNode =
        AssignmentNode(
            name = visit(ctx.assignLhs()) as LHSNode,
            value = visit(ctx.assignLhs()) as RHSNode,
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

    override fun visitSkipStat(ctx: WACCParser.SkipStatContext): ASTNode = SkipNode

    override fun visitReturnStat(ctx: WACCParser.ReturnStatContext): ASTNode =
        ReturnNode(
            value = visit(ctx.expr()) as ExprNode,
        )

    override fun visitDeclarationStat(ctx: WACCParser.DeclarationStatContext): ASTNode =
        DeclarationNode(
            type = visit(ctx.type()) as Type,
            name = ctx.IDENT().text,
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
            else -> visit(ctx.IDENT())
        }

    override fun visitAssignRhs(ctx: WACCParser.AssignRhsContext): ASTNode =
        when {
            ctx.expr() != null -> visit(ctx.expr())
            ctx.arrayLiter() != null -> visit(ctx.arrayLiter())
            ctx.pairLiter() != null -> visit(ctx.pairLiter())
            ctx.pairElem() != null -> visit(ctx.pairElem())
            else -> visit(ctx.funcCall())
        }

    override fun visitArgList(ctx: WACCParser.ArgListContext): ASTNode {
        TODO("Not yet implemented")
    }

    override fun visitType(ctx: WACCParser.TypeContext): ASTNode = when {
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
            else -> PairType()
        }

    override fun visitStrLiteral(ctx: WACCParser.StrLiteralContext): ASTNode {
        TODO("Not yet implemented")
    }

    override fun visitIdentifier(ctx: WACCParser.IdentifierContext): ASTNode =
        IdentifierNode(
            name = ctx.text
        )

    override fun visitUnaryOpExpr(ctx: WACCParser.UnaryOpExprContext): ASTNode {
        TODO("Not yet implemented")
    }

    override fun visitPairLiteral(ctx: WACCParser.PairLiteralContext): ASTNode {
        TODO("Not yet implemented")
    }

    override fun visitIntLiteral(ctx: WACCParser.IntLiteralContext): ASTNode =
        IntLiteral(
            value = ctx.text.toInt()
        )

    override fun visitBinOpExpr(ctx: WACCParser.BinOpExprContext): ASTNode {
        TODO("Not yet implemented")
    }

    override fun visitCharLiteral(ctx: WACCParser.CharLiteralContext): ASTNode {
        TODO("Not yet implemented")
    }

    override fun visitArrayElemExpr(ctx: WACCParser.ArrayElemExprContext): ASTNode {
        TODO("Not yet implemented")
    }

    override fun visitBoolLiteral(ctx: WACCParser.BoolLiteralContext): ASTNode {
        TODO("Not yet implemented")
    }

    override fun visitBracketedExpr(ctx: WACCParser.BracketedExprContext): ASTNode {
        TODO("Not yet implemented")
    }

    override fun visitUnaryOper(ctx: WACCParser.UnaryOperContext): ASTNode {
        TODO("Not yet implemented")
    }

    override fun visitBinaryOper(ctx: WACCParser.BinaryOperContext): ASTNode {
        TODO("Not yet implemented")
    }

    override fun visitPairElem(ctx: WACCParser.PairElemContext): ASTNode {
        TODO("Not yet implemented")
    }

    override fun visitArrayLiter(ctx: WACCParser.ArrayLiterContext): ASTNode {
        TODO("Not yet implemented")
    }

    override fun visitArrayElem(ctx: WACCParser.ArrayElemContext): ASTNode {
        TODO("Not yet implemented")
    }

    override fun visitFuncCall(ctx: WACCParser.FuncCallContext?): ASTNode {
        TODO("Not yet implemented")
    }

    override fun visitPairLiter(ctx: WACCParser.PairLiterContext?): ASTNode {
        TODO("Not yet implemented")
    }


}