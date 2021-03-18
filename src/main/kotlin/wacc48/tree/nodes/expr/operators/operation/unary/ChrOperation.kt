package wacc48.tree.nodes.expr.operators.operation.unary

import wacc48.shell.MemoryTable
import wacc48.tree.nodes.expr.CharLiteral
import wacc48.tree.nodes.expr.ExprNode
import wacc48.tree.nodes.expr.IntLiteral
import wacc48.tree.nodes.expr.Literal
import wacc48.tree.type.CharType
import wacc48.tree.type.IntType
import wacc48.tree.type.Type

object ChrOperation : UnaryOperation {
    override val repr: String = "chr"
    override val expectedExprTypes: List<Type> = listOf(IntType)
    override val returnType: Type = CharType

    override fun reduceToLiteral(mt: MemoryTable?, expr: ExprNode): Literal {
        val intExpr = expr.reduceToLiteral(mt) as IntLiteral
        return CharLiteral(intExpr.value.toChar(), null)
    }

    override fun reduceSingular(literal: Literal) =
        CharLiteral(
            value = (literal as IntLiteral).value.toChar(),
            ctx = literal.ctx
        )

}