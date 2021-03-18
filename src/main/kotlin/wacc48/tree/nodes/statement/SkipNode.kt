package wacc48.tree.nodes.statement

import wacc48.analyser.exceptions.Issue
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.ASTNode
import wacc48.tree.nodes.function.FuncNode

object SkipNode : StatNode {

    override val children: List<ASTNode>
        get() = emptyList()

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>,
        issues: MutableList<Issue>
    ) {
    }

    override fun toString(): String {
        return "Skip"
    }

    override fun <T> acceptVisitor(visitor: ASTVisitor<T>) : T {
        return visitor.defaultResult()
    }
}
