package tree.nodes.expr.operators

import analyser.exceptions.SemanticsException
import tree.SymbolTable
import tree.nodes.function.FuncNode
import generator.translator.CodeGeneratorVisitor
import org.antlr.v4.runtime.ParserRuleContext
import shell.MemoryTable
import tree.nodes.expr.*
import tree.type.*

data class BinOpNode(
    val operator: BinaryOperator,
    val firstExpr: ExprNode,
    val secondExpr: ExprNode,
    val ctx: ParserRuleContext?
) : ExprNode {
    override var type: Type = operator.returnType

    override fun reduceToLiteral(mt: MemoryTable?): Literal =
        when (operator) {
            BinaryOperator.EQ, BinaryOperator.NEQ -> reduceEqualityToLiteral(mt)
            BinaryOperator.AND,
            BinaryOperator.OR -> reduceLogicalToLiteral(mt)
            BinaryOperator.PLUS,
            BinaryOperator.MINUS,
            BinaryOperator.MULTIPLY,
            BinaryOperator.DIVIDE,
            BinaryOperator.MODULUS -> reduceArithmeticToLiteral(mt)
            else -> reduceComparatorToLiteral(mt)
        }

    private fun reduceEqualityToLiteral(mt: MemoryTable?): Literal {
        if (firstExpr is ArrayLiteral) {
            return when (operator) {
                BinaryOperator.EQ ->
                    BoolLiteral(firstExpr.literalToString() == (secondExpr as ArrayLiteral).literalToString(),
                        null)
                else ->
                    BoolLiteral(firstExpr.literalToString() != (secondExpr as ArrayLiteral).literalToString(),
                        null)
            }
        }

        when (firstExpr.type) {
            IntType -> {
                val firstIntExpr = firstExpr.reduceToLiteral(mt) as IntLiteral
                val secondIntExpr = secondExpr.reduceToLiteral(mt) as IntLiteral
                return when (operator) {
                    BinaryOperator.EQ -> BoolLiteral(firstIntExpr.value == secondIntExpr.value, null)
                    else -> BoolLiteral(firstIntExpr.value != secondIntExpr.value, null)
                }
            }
            BoolType -> {
                val firstBoolExpr = firstExpr.reduceToLiteral(mt) as BoolLiteral
                val secondBoolExpr = secondExpr.reduceToLiteral(mt) as BoolLiteral
                return when (operator) {
                    BinaryOperator.EQ -> BoolLiteral(firstBoolExpr.value == secondBoolExpr.value, null)
                    else -> BoolLiteral(firstBoolExpr.value != secondBoolExpr.value, null)
                }
            }
            CharType -> {
                val firstCharExpr = firstExpr.reduceToLiteral(mt) as CharLiteral
                val secondCharExpr = secondExpr.reduceToLiteral(mt) as CharLiteral
                return when (operator) {
                    BinaryOperator.EQ -> BoolLiteral(firstCharExpr.value == secondCharExpr.value, null)
                    else -> BoolLiteral(firstCharExpr.value != secondCharExpr.value, null)
                }
            }
            StringType -> {
                val firstStringExpr = firstExpr.reduceToLiteral(mt) as StringLiteral
                val secondStringExpr = secondExpr.reduceToLiteral(mt) as StringLiteral
                return when (operator) {
                    BinaryOperator.EQ -> BoolLiteral(firstStringExpr.value == secondStringExpr.value, null)
                    else -> BoolLiteral(firstStringExpr.value != secondStringExpr.value, null)
                }
            }
            is ArrayType -> {
                val firstArrExpr = firstExpr.reduceToLiteral(mt)
                val secondArrExpr = secondExpr.reduceToLiteral(mt)
                return BinOpNode(operator, firstArrExpr, secondArrExpr, null).reduceToLiteral()
            }
            else -> { //Pair type
                return when (operator) {
                    BinaryOperator.EQ ->
                        BoolLiteral((firstExpr as IdentifierNode).name == (secondExpr as IdentifierNode).name,
                            null)
                    else ->
                        BoolLiteral((firstExpr as IdentifierNode).name != (secondExpr as IdentifierNode).name,
                            null)
                }

            }
        }

    }

    private fun reduceArithmeticToLiteral(mt: MemoryTable?): Literal {
        val firstIntExpr = firstExpr.reduceToLiteral(mt) as IntLiteral
        val secondIntExpr = secondExpr.reduceToLiteral(mt) as IntLiteral
        return when (operator) {
            BinaryOperator.PLUS -> IntLiteral(firstIntExpr.value + secondIntExpr.value, null)
            BinaryOperator.MINUS -> IntLiteral(firstIntExpr.value - secondIntExpr.value, null)
            BinaryOperator.MULTIPLY -> IntLiteral(firstIntExpr.value * secondIntExpr.value, null)
            BinaryOperator.DIVIDE -> IntLiteral(firstIntExpr.value / secondIntExpr.value, null)
            else -> IntLiteral(firstIntExpr.value % secondIntExpr.value, null)
        }
    }

    private fun reduceLogicalToLiteral(mt: MemoryTable?): Literal {
        val firstBoolExpr = firstExpr.reduceToLiteral(mt) as BoolLiteral
        val secondBoolExpr = secondExpr.reduceToLiteral(mt) as BoolLiteral
        if (operator == BinaryOperator.AND) {
            return BoolLiteral(firstBoolExpr.value && secondBoolExpr.value, null)
        }
        return BoolLiteral(firstBoolExpr.value || secondBoolExpr.value, null)
    }

    private fun reduceComparatorToLiteral(mt: MemoryTable?): Literal {
        if (firstExpr.type is IntType) {
            val firstIntExpr = firstExpr.reduceToLiteral(mt) as IntLiteral
            val secondIntExpr = secondExpr.reduceToLiteral(mt) as IntLiteral
            return when (operator) {
                BinaryOperator.GT -> BoolLiteral(firstIntExpr.value > secondIntExpr.value, null)
                BinaryOperator.LT -> BoolLiteral(firstIntExpr.value < secondIntExpr.value, null)
                BinaryOperator.GE -> BoolLiteral(firstIntExpr.value >= secondIntExpr.value, null)
                else -> BoolLiteral(firstIntExpr.value <= secondIntExpr.value, null)
            }
        } else { //Char type
            val firstCharExpr = firstExpr.reduceToLiteral(mt) as CharLiteral
            val secondCharExpr = secondExpr.reduceToLiteral(mt) as CharLiteral
            return when (operator) {
                BinaryOperator.GT -> BoolLiteral(firstCharExpr.value > secondCharExpr.value, null)
                BinaryOperator.LT -> BoolLiteral(firstCharExpr.value < secondCharExpr.value, null)
                BinaryOperator.GE -> BoolLiteral(firstCharExpr.value >= secondCharExpr.value, null)
                else -> BoolLiteral(firstCharExpr.value <= secondCharExpr.value, null)
            }
        }
    }

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {

        firstExpr.validate(st, funTable)
        secondExpr.validate(st, funTable)

        val expected = operator.expectedExprTypes

        if (expected.isNotEmpty()) {
            if (firstExpr.type !in expected)
                throw SemanticsException(
                    "Type-mismatched on operator $operator: arg 1 has type " +
                            "${firstExpr.type}, required 1 of type(s) $expected",
                    ctx
                )
        }
        if (firstExpr.type != secondExpr.type)
            throw SemanticsException(
                "Type-mismatched on operator $operator: arg 1 has type " +
                        "${firstExpr.type}, arg 2 of type(s) ${secondExpr.type}",
                ctx
            )
    }

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateBinOp(this)
    }
}


enum class BinaryOperator(
    val repr: String,
    val expectedExprTypes: List<Type>,
    val returnType: Type
) {
    PLUS("+", listOf(IntType), IntType),
    MINUS("-", listOf(IntType), IntType),
    MULTIPLY("*", listOf(IntType), IntType),
    DIVIDE("/", listOf(IntType), IntType),
    MODULUS("%", listOf(IntType), IntType),
    GT(">", listOf(IntType, CharType), BoolType),
    LT("<", listOf(IntType, CharType), BoolType),
    GE(">=", listOf(IntType, CharType), BoolType),
    LE("<=", listOf(IntType, CharType), BoolType),
    EQ("==", emptyList(), BoolType),
    NEQ("!=", emptyList(), BoolType),
    AND("&&", listOf(BoolType), BoolType),
    OR("||", listOf(BoolType), BoolType);

    companion object {
        fun lookupRepresentation(string: String) =
            values().firstOrNull { it.repr == string }

    }

}