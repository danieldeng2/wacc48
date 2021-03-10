package tree.nodes.expr

import shell.MemoryTable
import tree.nodes.assignment.RHSNode

interface ExprNode : RHSNode {
    fun reduceToLiteral(mt: MemoryTable? = null): Literal
}
