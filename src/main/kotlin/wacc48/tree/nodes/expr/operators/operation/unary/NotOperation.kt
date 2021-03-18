package wacc48.tree.nodes.expr.operators.operation.unary

import wacc48.shell.MemoryTable
import wacc48.tree.nodes.expr.BoolLiteral
import wacc48.tree.nodes.expr.ExprNode
import wacc48.tree.nodes.expr.IntLiteral
import wacc48.tree.nodes.expr.Literal
import wacc48.tree.type.BoolType
import wacc48.tree.type.Type

object NotOperation : UnaryOperation {
    override val repr: String = "!"
    override val expectedExprTypes: List<Type> = listOf(BoolType)
    override val returnType: Type = BoolType

    override fun reduceToLiteral(mt: MemoryTable?, expr: ExprNode): Literal {
        val boolExpr = expr.reduceToLiteral(mt) as BoolLiteral
        return BoolLiteral(!boolExpr.value, null)
    }

    override fun reduceSingular(literal: Literal) =
        BoolLiteral(
            value = !(literal as BoolLiteral).value,
            ctx = null
        )
}