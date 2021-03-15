package wacc48.tree.nodes.expr

import wacc48.shell.MemoryTable

interface Literal : ExprNode {
    fun literalToString(mt: MemoryTable? = null): String

    override fun reduceToLiteral(mt: MemoryTable?): Literal = this
}