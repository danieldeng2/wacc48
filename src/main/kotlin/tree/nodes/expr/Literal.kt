package tree.nodes.expr

import shell.MemoryTable

interface Literal : ExprNode {
    fun literalToString(mt: MemoryTable? = null): String

    override fun reduceToLiteral(mt: MemoryTable?): Literal = this
}