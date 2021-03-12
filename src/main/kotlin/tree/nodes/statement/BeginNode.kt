package tree.nodes.statement

import generator.translator.CodeGeneratorVisitor
import org.antlr.v4.runtime.ParserRuleContext
import shell.CodeEvaluatorVisitor
import tree.SymbolTable
import tree.nodes.expr.Literal
import tree.nodes.function.FuncNode

data class BeginNode(
    val stat: StatNode,
    val ctx: ParserRuleContext?,
) : StatNode {
    lateinit var currST: SymbolTable

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.currST = SymbolTable(st)
        stat.validate(currST, funTable)
    }

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateBegin(this)
    }

    override fun acceptCodeEvalVisitor(visitor: CodeEvaluatorVisitor) {
        visitor.translateBegin(this)
    }
}