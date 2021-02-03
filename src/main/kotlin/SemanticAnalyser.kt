/*
import analyser.SymbolTable
import analyser.nodes.*
import analyser.nodes.function.ExitNode
import analyser.nodes.function.FuncNode
import analyser.nodes.function.ParamListNode
import analyser.nodes.function.ParamNode
import analyser.nodes.expr.ExprNode
import analyser.nodes.statement.SkipNode
import analyser.nodes.statement.StatNode
import analyser.nodes.type.*
import antlr.WACCParser
import antlr.WACCParserBaseVisitor

class SemanticAnalyser : WACCParserBaseVisitor<ASTNode>() {

    private var st: SymbolTable = SymbolTable(null)

    override fun visitProg(ctx: WACCParser.ProgContext): ASTNode {
        println("Entering program...")
        val funcList = mutableListOf<FuncNode>()

        for (func in ctx.func()) {
            val funcId = func.IDENT().text
            if (st.lookupCurrentScope(funcId) != null) return ErrorNode()

            val paramList = when {
                func.paramList() == null -> ParamListNode(emptyList())
                else -> visit(func.paramList())
            }

            if (paramList !is ParamListNode) return ErrorNode()
            val funcNode = FuncNode(funcId, paramList)

            st.add(funcId, funcNode)
            funcList.add(funcNode)

            // TODO: Redeclaration of function
            return ErrorNode()

        }

        visitChildren(ctx)

        val statNode = visit(ctx.stat())
        if (statNode is StatNode) {
            return ProgNode(statNode, funcList)
        }

        // TODO: Replace error handling
        println("Error parsing program node!")
        return ErrorNode()
    }

    override fun visitFunc(ctx: WACCParser.FuncContext): ASTNode {
        println("Visiting ${ctx.IDENT().text}")
        val retTypeNode = visit(ctx.type())
        if (retTypeNode !is Type) {
            return ErrorNode()
        }

        val funcNode = st.lookupCurrentScope(ctx.IDENT().text) as FuncNode
        val globalScope = st
        val funcDecScope = SymbolTable(st)

        st = SymbolTable(funcDecScope)

        val statNode = visit(ctx.stat())
        if (statNode !is StatNode) {
            return ErrorNode()
        }

        st = globalScope
        funcNode.retType = retTypeNode
        funcNode.statNode = statNode

        if (funcNode.isValid(st)) {
            println("Function ${funcNode.identifier} added to symbol table.")
            return funcNode
        }

        //TODO: Error handling
        return ErrorNode()
    }

    override fun visitParamList(ctx: WACCParser.ParamListContext): ASTNode {
        val params = mutableListOf<ParamNode>()
        for ((_, paramCtx) in ctx.param().withIndex()) {
            if (st.lookupCurrentScope(paramCtx.IDENT().text) != null)
                return ErrorNode()

            val type = visit(paramCtx.type()) as Type
            val param = ParamNode(type, paramCtx.IDENT().text)
            st.add(paramCtx.IDENT().text, param)
            params.add(param)
        }

        return ErrorNode()
    }

    override fun visitType(ctx: WACCParser.TypeContext): ASTNode {
        if (ctx.type() != null) {
            val t = visit(ctx.type()) as Type
            return ArrayType(t)
        }

        if (ctx.baseType() != null) return visit(ctx.baseType())
        return visit(ctx.pairType())
    }

    override fun visitBaseType(ctx: WACCParser.BaseTypeContext): ASTNode =
        when {
            ctx.BOOL() != null -> BoolType
            ctx.CHAR() != null -> CharType
            ctx.STRING() != null -> StringType
            else -> IntType
        }

    override fun visitPairType(ctx: WACCParser.PairTypeContext): ASTNode {
        val lhs = visit(ctx.pairElemType(0)) as Type
        val rhs = visit(ctx.pairElemType(1)) as Type

        return PairType(lhs, rhs)
    }

    override fun visitPairElemType(ctx: WACCParser.PairElemTypeContext):
            ASTNode {
        if (ctx.baseType() != null) return visit(ctx.baseType())
        if (ctx.type() != null) return visit(ctx.type())

        assert(ctx.PAIR() != null)
        return PairType()
    }

    override fun visitSkipStat(ctx: WACCParser.SkipStatContext): ASTNode {
        return SkipNode()
    }

    override fun visitExitStat(ctx: WACCParser.ExitStatContext): ASTNode {
        val node = visit(ctx.expr())

        // TODO: check if expr evaluates to an int
        if (node is ExprNode && node.isValid(st)) {
            return ExitNode()
        }
        return ErrorNode()
    }
}*/
