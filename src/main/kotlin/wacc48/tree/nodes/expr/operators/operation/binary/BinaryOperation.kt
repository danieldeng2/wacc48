package wacc48.tree.nodes.expr.operators.operation.binary

import wacc48.shell.MemoryTable
import wacc48.tree.nodes.expr.ExprNode
import wacc48.tree.nodes.expr.Literal
import wacc48.tree.nodes.expr.operators.operation.Operation

interface BinaryOperation: Operation {
    fun reduceToLiteral(
        mt: MemoryTable?,
        firstExpr: ExprNode,
        secondExpr: ExprNode,
    ): Literal

    fun reduceIntegers(firstInt: Int, secondInt: Int): Literal
}