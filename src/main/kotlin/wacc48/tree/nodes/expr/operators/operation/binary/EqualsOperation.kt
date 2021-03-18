package wacc48.tree.nodes.expr.operators.operation.binary

import wacc48.shell.MemoryTable
import wacc48.shell.ShellRunTimeException
import wacc48.tree.nodes.expr.ArrayLiteral
import wacc48.tree.nodes.expr.BoolLiteral
import wacc48.tree.nodes.expr.CharLiteral
import wacc48.tree.nodes.expr.ExprNode
import wacc48.tree.nodes.expr.IdentifierNode
import wacc48.tree.nodes.expr.IntLiteral
import wacc48.tree.nodes.expr.Literal
import wacc48.tree.nodes.expr.PairLiteral
import wacc48.tree.nodes.expr.PairMemoryLiteral
import wacc48.tree.nodes.expr.StringLiteral
import wacc48.tree.nodes.expr.operators.BinOpNode
import wacc48.tree.type.ArrayType
import wacc48.tree.type.BoolType
import wacc48.tree.type.CharType
import wacc48.tree.type.IntType
import wacc48.tree.type.PairType
import wacc48.tree.type.StringType
import wacc48.tree.type.Type

object EqualsOperation : BinaryOperation {
    override val repr: String = "=="
    override val expectedExprTypes: List<Type> = emptyList()
    override val returnType: Type = BoolType

    override fun reduceToLiteral(mt: MemoryTable?, firstExpr: ExprNode, secondExpr: ExprNode): Literal {
        if (firstExpr.type != secondExpr.type)
            throw ShellRunTimeException("Cannot compare type ${firstExpr.type} with type ${secondExpr.type}")

        if (firstExpr is ArrayLiteral) {
            return BoolLiteral(
                firstExpr.literalToString(mt) == (secondExpr as ArrayLiteral).literalToString(mt),
                null
            )
        }

        return when (firstExpr.type) {
            IntType -> {
                val firstIntExpr = firstExpr.reduceToLiteral(mt) as IntLiteral
                val secondIntExpr = secondExpr.reduceToLiteral(mt) as IntLiteral
                BoolLiteral(firstIntExpr.value == secondIntExpr.value, null)
            }
            BoolType -> {
                val firstBoolExpr = firstExpr.reduceToLiteral(mt) as BoolLiteral
                val secondBoolExpr = secondExpr.reduceToLiteral(mt) as BoolLiteral
                BoolLiteral(firstBoolExpr.value == secondBoolExpr.value, null)
            }
            CharType -> {
                val firstCharExpr = firstExpr.reduceToLiteral(mt) as CharLiteral
                val secondCharExpr = secondExpr.reduceToLiteral(mt) as CharLiteral
                BoolLiteral(firstCharExpr.value == secondCharExpr.value, null)
            }
            StringType -> {
                val firstStringExpr = firstExpr.reduceToLiteral(mt) as StringLiteral
                val secondStringExpr = secondExpr.reduceToLiteral(mt) as StringLiteral
                BoolLiteral(firstStringExpr.value == secondStringExpr.value,null)
            }
            is ArrayType -> {
                val firstArrExpr = firstExpr.reduceToLiteral(mt)
                val secondArrExpr = secondExpr.reduceToLiteral(mt)
                BinOpNode(this, firstArrExpr, secondArrExpr, null).reduceToLiteral(mt)
            }
            else -> { //Pair type
                val firstPair = if (firstExpr is IdentifierNode) mt?.getLiteral(firstExpr.name) else PairLiteral
                val secondPair = if (secondExpr is IdentifierNode) mt?.getLiteral(secondExpr.name) else PairLiteral
                if (firstPair is PairMemoryLiteral && secondPair is PairMemoryLiteral)
                    BoolLiteral(firstPair == secondPair, null)
                else
                    BoolLiteral(!(firstPair is PairLiteral && secondPair is PairLiteral), null)
            }
        }
    }

    override fun reduceIntegers(firstInt: Int, secondInt: Int): Literal =
        BoolLiteral(
            value = firstInt == secondInt,
            ctx = null
        )
}