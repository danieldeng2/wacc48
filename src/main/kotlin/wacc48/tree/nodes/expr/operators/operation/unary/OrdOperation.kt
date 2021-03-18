package wacc48.tree.nodes.expr.operators.operation.unary

import wacc48.shell.MemoryTable
import wacc48.tree.nodes.expr.CharLiteral
import wacc48.tree.nodes.expr.ExprNode
import wacc48.tree.nodes.expr.IntLiteral
import wacc48.tree.nodes.expr.Literal
import wacc48.tree.type.CharType
import wacc48.tree.type.IntType
import wacc48.tree.type.Type

object OrdOperation : UnaryOperation {
    override val repr: String = "ord"
    override val expectedExprTypes: List<Type> = listOf(CharType)
    override val returnType: Type = IntType

    override fun reduceToLiteral(mt: MemoryTable?, expr: ExprNode): Literal {
        val charExpr = expr.reduceToLiteral(mt) as CharLiteral
        return IntLiteral(charExpr.value.toInt(), false, null)
    }

    override fun reduceSingular(literal: Literal): Literal =
        IntLiteral(
            value = (literal as CharLiteral).value.toInt(),
            ctx = null
        )
}