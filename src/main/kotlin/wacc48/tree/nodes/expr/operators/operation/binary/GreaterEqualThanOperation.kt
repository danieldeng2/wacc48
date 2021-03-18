package wacc48.tree.nodes.expr.operators.operation.binary

import wacc48.shell.MemoryTable
import wacc48.tree.nodes.expr.BoolLiteral
import wacc48.tree.nodes.expr.CharLiteral
import wacc48.tree.nodes.expr.ExprNode
import wacc48.tree.nodes.expr.IntLiteral
import wacc48.tree.nodes.expr.Literal
import wacc48.tree.type.BoolType
import wacc48.tree.type.CharType
import wacc48.tree.type.IntType
import wacc48.tree.type.Type

object GreaterEqualThanOperation : BinaryOperation {
    override val repr: String = ">="
    override val expectedExprTypes: List<Type> = listOf(IntType, CharType)
    override val returnType: Type = BoolType

    override fun reduceToLiteral(mt: MemoryTable?, firstExpr: ExprNode, secondExpr: ExprNode): Literal {
        if (firstExpr.type is IntType) {
            val firstVal = (firstExpr.reduceToLiteral(mt) as IntLiteral).value
            val secondVal = (secondExpr.reduceToLiteral(mt) as IntLiteral).value
            return BoolLiteral(firstVal >= secondVal, null)
        }
        val firstVal = (firstExpr.reduceToLiteral(mt) as CharLiteral).value
        val secondVal = (secondExpr.reduceToLiteral(mt) as CharLiteral).value
        return BoolLiteral(firstVal >= secondVal, null)
    }

    override fun reduceIntegers(firstInt: Int, secondInt: Int): Literal =
        BoolLiteral(
            value = firstInt >= secondInt,
            ctx = null
        )
}