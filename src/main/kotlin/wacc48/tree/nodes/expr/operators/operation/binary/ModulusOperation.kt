package wacc48.tree.nodes.expr.operators.operation.binary

import wacc48.shell.MemoryTable
import wacc48.shell.ShellDivideByZeroException
import wacc48.shell.detectIntegerOverflow
import wacc48.tree.nodes.expr.ExprNode
import wacc48.tree.nodes.expr.IntLiteral
import wacc48.tree.nodes.expr.Literal
import wacc48.tree.type.IntType
import wacc48.tree.type.Type

object ModulusOperation : BinaryOperation {
    override val repr: String = "%"
    override val expectedExprTypes: List<Type> = listOf(IntType)
    override val returnType: Type = IntType

    override fun reduceToLiteral(mt: MemoryTable?, firstExpr: ExprNode, secondExpr: ExprNode): Literal {
        val firstVal = (firstExpr.reduceToLiteral(mt) as IntLiteral).value
        val secondVal = (secondExpr.reduceToLiteral(mt) as IntLiteral).value

        detectIntegerOverflow(firstVal, secondVal, this)

        if (secondVal == 0)
            throw ShellDivideByZeroException("mod by zero not allowed")

        return IntLiteral(firstVal / secondVal, false, null)
    }

    override fun reduceIntegers(firstInt: Int, secondInt: Int) = IntLiteral(
        value = firstInt % secondInt,
        ctx = null,
    )
}