package wacc48.tree.nodes.statement

import wacc48.analyser.exceptions.Issue
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.function.FuncNode

object SkipNode : StatNode {

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>,
        issues: MutableList<Issue>
    ) {
    }

    override fun toString(): String {
        return "Skip"
    }

    override fun acceptVisitor(visitor: ASTVisitor) {}
}
