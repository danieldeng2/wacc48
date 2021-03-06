package wacc48.tree.nodes

import wacc48.analyser.exceptions.Issue
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.function.FuncNode

interface ASTNode {

    val children: List<ASTNode>

    fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>,
        issues: MutableList<Issue>
    )

    fun <T> acceptVisitor(visitor: ASTVisitor<T>) : T
}
