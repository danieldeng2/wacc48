package tree.nodes.statement

import tree.SymbolTable
import tree.nodes.function.FuncNode
import tree.ASTVisitor

object SkipNode : StatNode {

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
    }

    override fun toString(): String {
        return "Skip"
    }

    override fun acceptVisitor(visitor: ASTVisitor) {}
}
