package wacc48.tree.nodes.expr

import wacc48.shell.MemoryTable
import wacc48.tree.nodes.assignment.RHSNode

interface ExprNode : RHSNode {
    fun reduceToLiteral(mt: MemoryTable? = null): Literal
}
