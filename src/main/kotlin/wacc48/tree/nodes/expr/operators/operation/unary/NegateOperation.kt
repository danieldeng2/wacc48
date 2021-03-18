package wacc48.tree.nodes.expr.operators.operation.unary

import wacc48.shell.MemoryTable
import wacc48.shell.detectIntegerOverflow
import wacc48.tree.nodes.expr.CharLiteral
import wacc48.tree.nodes.expr.ExprNode
import wacc48.tree.nodes.expr.IntLiteral
import wacc48.tree.nodes.expr.Literal
import wacc48.tree.nodes.expr.operators.operation.binary.MinusOperation
import wacc48.tree.type.IntType
import wacc48.tree.type.Type

object NegateOperation : UnaryOperation {
    override val repr: String = "-"
    override val expectedExprTypes: List<Type> = listOf(IntType)
    override val returnType: Type = IntType

    override fun reduceToLiteral(mt: MemoryTable?, expr: ExprNode): Literal {
        val intExpr = expr.reduceToLiteral(mt) as IntLiteral
        detectIntegerOverflow(0, intExpr.value, MinusOperation)
        return IntLiteral(-intExpr.value, false, null)
    }

    override fun reduceSingular(literal: Literal) =
        IntLiteral(
            value = -(literal as IntLiteral).value,
            ctx = null
        )
}