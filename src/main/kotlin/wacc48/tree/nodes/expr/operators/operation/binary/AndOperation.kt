package wacc48.tree.nodes.expr.operators.operation.binary

import wacc48.shell.MemoryTable
import wacc48.tree.nodes.expr.BoolLiteral
import wacc48.tree.nodes.expr.ExprNode
import wacc48.tree.nodes.expr.Literal
import wacc48.tree.type.BoolType
import wacc48.tree.type.Type

object AndOperation : BinaryOperation {
    override val repr: String = "&&"
    override val expectedExprTypes: List<Type> = listOf(BoolType)
    override val returnType: Type = BoolType

    override fun reduceToLiteral(mt: MemoryTable?, firstExpr: ExprNode, secondExpr: ExprNode): Literal {
        val firstVal = (firstExpr.reduceToLiteral(mt) as BoolLiteral).value
        val secondVal = (secondExpr.reduceToLiteral(mt) as BoolLiteral).value
        return BoolLiteral(firstVal && secondVal, null)
    }

    override fun reduceIntegers(firstInt: Int, secondInt: Int) =
        BoolLiteral(
            value = firstInt == 1 && secondInt == 1,
            ctx = null
        )
}