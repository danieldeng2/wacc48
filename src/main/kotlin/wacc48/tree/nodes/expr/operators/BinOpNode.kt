package wacc48.tree.nodes.expr.operators

import org.antlr.v4.runtime.ParserRuleContext
import wacc48.analyser.exceptions.Issue
import wacc48.analyser.exceptions.addSemantic
import wacc48.shell.*
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.ASTNode
import wacc48.tree.nodes.expr.*
import wacc48.tree.nodes.function.FuncNode
import wacc48.tree.type.*

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

    override val children: List<ASTNode>
        get() = listOf(firstExpr, secondExpr)

    private fun reduceEqualityToLiteral(mt: MemoryTable?): Literal {
        if (firstExpr.type != secondExpr.type)
            throw ShellRunTimeException("Cannot compare type ${firstExpr.type} with type ${secondExpr.type}")

        if (firstExpr is ArrayLiteral) {
            return when (operator) {
                BinaryOperator.EQ ->
                    BoolLiteral(
                        firstExpr.literalToString() == (secondExpr as ArrayLiteral).literalToString(),
                        null
                    )
                else ->
                    BoolLiteral(
                        firstExpr.literalToString() != (secondExpr as ArrayLiteral).literalToString(),
                        null
                    )
            }
        }

        when (firstExpr.type) {
            IntType -> {
                val firstIntExpr = firstExpr.reduceToLiteral(mt) as IntLiteral
                val secondIntExpr = secondExpr.reduceToLiteral(mt) as IntLiteral
                return when (operator) {
                    BinaryOperator.EQ -> BoolLiteral(
                        firstIntExpr.value == secondIntExpr.value,
                        null
                    )
                    else -> BoolLiteral(
                        firstIntExpr.value != secondIntExpr.value,
                        null
                    )
                }
            }
            BoolType -> {
                val firstBoolExpr = firstExpr.reduceToLiteral(mt) as BoolLiteral
                val secondBoolExpr =
                    secondExpr.reduceToLiteral(mt) as BoolLiteral
                return when (operator) {
                    BinaryOperator.EQ -> BoolLiteral(
                        firstBoolExpr.value == secondBoolExpr.value,
                        null
                    )
                    else -> BoolLiteral(
                        firstBoolExpr.value != secondBoolExpr.value,
                        null
                    )
                }
            }
            CharType -> {
                val firstCharExpr = firstExpr.reduceToLiteral(mt) as CharLiteral
                val secondCharExpr =
                    secondExpr.reduceToLiteral(mt) as CharLiteral
                return when (operator) {
                    BinaryOperator.EQ -> BoolLiteral(
                        firstCharExpr.value == secondCharExpr.value,
                        null
                    )
                    else -> BoolLiteral(
                        firstCharExpr.value != secondCharExpr.value,
                        null
                    )
                }
            }
            StringType -> {
                val firstStringExpr =
                    firstExpr.reduceToLiteral(mt) as StringLiteral
                val secondStringExpr =
                    secondExpr.reduceToLiteral(mt) as StringLiteral
                return when (operator) {
                    BinaryOperator.EQ -> BoolLiteral(
                        firstStringExpr.value == secondStringExpr.value,
                        null
                    )
                    else -> BoolLiteral(
                        firstStringExpr.value != secondStringExpr.value,
                        null
                    )
                }
            }
            is ArrayType -> {
                val firstArrExpr = firstExpr.reduceToLiteral(mt)
                val secondArrExpr = secondExpr.reduceToLiteral(mt)
                return BinOpNode(
                    operator,
                    firstArrExpr,
                    secondArrExpr,
                    null
                ).reduceToLiteral()
            }
            else -> { //Pair type
                val firstPair =
                    if (firstExpr is IdentifierNode) mt?.getLiteral(firstExpr.name) else PairLiteral
                val secondPair =
                    if (secondExpr is IdentifierNode) mt?.getLiteral(secondExpr.name) else PairLiteral
                return when (operator) {
                    BinaryOperator.EQ ->
                        if (firstPair is PairMemoryLiteral && secondPair is PairMemoryLiteral)
                            BoolLiteral(
                                firstPair == secondPair,
                                null
                            )
                        else
                            BoolLiteral(
                                (firstPair is PairLiteral && secondPair is PairLiteral),
                                null
                            )
                    else ->
                        if (firstPair is PairMemoryLiteral && secondPair is PairMemoryLiteral)
                            BoolLiteral(
                                firstPair != secondPair,
                                null
                            )
                        else
                            BoolLiteral(
                                !(firstPair is PairLiteral && secondPair is PairLiteral),
                                null
                            )
                }

            }
        }

    }

    private fun reduceArithmeticToLiteral(mt: MemoryTable?): Literal {
        val firstIntExpr = firstExpr.reduceToLiteral(mt) as IntLiteral
        val secondIntExpr = secondExpr.reduceToLiteral(mt) as IntLiteral

        detectIntegerOverflow(firstIntExpr.value, secondIntExpr.value, operator)

        return when (operator) {
            BinaryOperator.PLUS -> {
                if (firstIntExpr.value + secondIntExpr.value > Int.MAX_VALUE)
                    throw ShellIntegerOverflowException("addition overflow")
                IntLiteral(
                    firstIntExpr.value + secondIntExpr.value,
                    false,
                    null
                )
            }
            BinaryOperator.MINUS -> IntLiteral(
                firstIntExpr.value - secondIntExpr.value,
                false,
                null
            )
            BinaryOperator.MULTIPLY -> IntLiteral(
                firstIntExpr.value * secondIntExpr.value,
                false,
                null
            )
            BinaryOperator.DIVIDE -> {
                if (secondIntExpr.value == 0)
                    throw ShellDivideByZeroException("divide by zero not allowed")
                IntLiteral(
                    firstIntExpr.value / secondIntExpr.value,
                    false,
                    null
                )
            }
            else -> {
                if (secondIntExpr.value == 0)
                    throw ShellDivideByZeroException("mod by zero not allowed")
                IntLiteral(
                    firstIntExpr.value % secondIntExpr.value,
                    false,
                    null
                )
            }
        }
    }

    private fun reduceLogicalToLiteral(mt: MemoryTable?): Literal {
        val firstBoolExpr = firstExpr.reduceToLiteral(mt) as BoolLiteral
        val secondBoolExpr = secondExpr.reduceToLiteral(mt) as BoolLiteral
        if (operator == BinaryOperator.AND)
            return BoolLiteral(
                firstBoolExpr.value && secondBoolExpr.value,
                null
            )
        return BoolLiteral(firstBoolExpr.value || secondBoolExpr.value, null)
    }

    private fun reduceComparatorToLiteral(mt: MemoryTable?): Literal {
        if (firstExpr.type is IntType) {
            val firstIntExpr = firstExpr.reduceToLiteral(mt) as IntLiteral
            val secondIntExpr = secondExpr.reduceToLiteral(mt) as IntLiteral
            return when (operator) {
                BinaryOperator.GT -> BoolLiteral(
                    firstIntExpr.value > secondIntExpr.value,
                    null
                )
                BinaryOperator.LT -> BoolLiteral(
                    firstIntExpr.value < secondIntExpr.value,
                    null
                )
                BinaryOperator.GE -> BoolLiteral(
                    firstIntExpr.value >= secondIntExpr.value,
                    null
                )
                else -> BoolLiteral(
                    firstIntExpr.value <= secondIntExpr.value,
                    null
                )
            }
        } else { //Char type
            val firstCharExpr = firstExpr.reduceToLiteral(mt) as CharLiteral
            val secondCharExpr = secondExpr.reduceToLiteral(mt) as CharLiteral
            return when (operator) {
                BinaryOperator.GT -> BoolLiteral(
                    firstCharExpr.value > secondCharExpr.value,
                    null
                )
                BinaryOperator.LT -> BoolLiteral(
                    firstCharExpr.value < secondCharExpr.value,
                    null
                )
                BinaryOperator.GE -> BoolLiteral(
                    firstCharExpr.value >= secondCharExpr.value,
                    null
                )
                else -> BoolLiteral(
                    firstCharExpr.value <= secondCharExpr.value,
                    null
                )
            }
        }
    }

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>,
        issues: MutableList<Issue>
    ) {

        firstExpr.validate(st, funTable, issues)
        secondExpr.validate(st, funTable, issues)

        val expected = operator.expectedExprTypes

        if (expected.isNotEmpty()) {
            if (firstExpr.type !in expected)
                issues.addSemantic(
                    "Type-mismatched on operator $operator: arg 1 has type " +
                            "${firstExpr.type}, required 1 of type(s) $expected",
                    ctx
                )
        }
        if (firstExpr.type != secondExpr.type)
            issues.addSemantic(
                "Type-mismatched on operator $operator: arg 1 has type " +
                        "${firstExpr.type}, arg 2 of type(s) ${secondExpr.type}",
                ctx
            )
    }

    override fun <T> acceptVisitor(visitor: ASTVisitor<T>): T {
        return visitor.visitBinOp(this)
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