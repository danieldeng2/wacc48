package wacc48.tree.nodes.statement

import org.antlr.v4.runtime.ParserRuleContext
import wacc48.analyser.exceptions.Issue
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.function.FuncNode

data class BeginNode(
    var stat: StatNode,
    val ctx: ParserRuleContext?,
) : StatNode {
    lateinit var currST: SymbolTable

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>,
        issues: MutableList<Issue>
    ) {
        this.currST = SymbolTable(st)
        stat.validate(currST, funTable, issues)
    }

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitBegin(this)
    }
}