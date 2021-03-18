package wacc48.tree.nodes.expr.operators.operation.unary

import wacc48.shell.MemoryTable
import wacc48.tree.nodes.expr.ArrayLiteral
import wacc48.tree.nodes.expr.ExprNode
import wacc48.tree.nodes.expr.IntLiteral
import wacc48.tree.nodes.expr.Literal
import wacc48.tree.nodes.expr.StringLiteral
import wacc48.tree.type.ArrayType
import wacc48.tree.type.IntType
import wacc48.tree.type.StringType
import wacc48.tree.type.Type
import wacc48.tree.type.VoidType

object LenOperation : UnaryOperation {
    override val repr: String = "len"
    override val expectedExprTypes: List<Type> = listOf(StringType, ArrayType(VoidType))
    override val returnType: Type = IntType

    override fun reduceToLiteral(mt: MemoryTable?, expr: ExprNode): Literal =
        if (expr.type is StringType) {
            val strExpr = expr.reduceToLiteral(mt) as StringLiteral
            IntLiteral(strExpr.value.length, false, null)
        } else { //Array type
            val arrExpr = expr.reduceToLiteral(mt) as ArrayLiteral
            IntLiteral(arrExpr.values.size, false, null)
        }

    // Returns the same thing as hard to optimise
    override fun reduceSingular(literal: Literal): Literal = literal
}