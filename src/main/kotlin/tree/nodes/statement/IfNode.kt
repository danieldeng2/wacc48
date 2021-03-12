package tree.nodes.statement

import analyser.exceptions.SemanticsException
import tree.SymbolTable
import tree.nodes.expr.ExprNode
import tree.nodes.function.FuncNode
import tree.type.BoolType
import generator.translator.CodeGeneratorVisitor
import org.antlr.v4.runtime.ParserRuleContext
import shell.CodeEvaluatorVisitor
import tree.nodes.expr.Literal

data class IfNode(
    val proposition: ExprNode,
    val trueStat: StatNode,
    val falseStat: StatNode,
    val ctx: ParserRuleContext?,
) : StatNode {
    lateinit var trueST: SymbolTable
    lateinit var falseST: SymbolTable

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.trueST = SymbolTable(st)
        this.falseST = SymbolTable(st)

        if (proposition.type != BoolType)
            throw SemanticsException(
                "If statement proposition must be boolean",
                ctx
            )

        proposition.validate(st, funTable)
        trueStat.validate(trueST, funTable)
        falseStat.validate(falseST, funTable)
    }

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateIf(this)
    }

    override fun acceptCodeEvalVisitor(visitor: CodeEvaluatorVisitor): Literal? {
        return visitor.translateIf(this)
    }
}