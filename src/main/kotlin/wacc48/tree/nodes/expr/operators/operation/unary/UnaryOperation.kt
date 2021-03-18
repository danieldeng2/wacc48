package wacc48.tree.nodes.expr.operators.operation.unary

import wacc48.shell.MemoryTable
import wacc48.tree.nodes.expr.ExprNode
import wacc48.tree.nodes.expr.Literal
import wacc48.tree.nodes.expr.operators.operation.Operation

interface UnaryOperation : Operation {
    fun reduceToLiteral(mt: MemoryTable?, expr: ExprNode): Literal

    fun reduceSingular(literal: Literal) : Literal
}