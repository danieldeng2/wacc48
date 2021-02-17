package analyser.nodes.expr.operators

import analyser.SymbolTable
import analyser.nodes.expr.ExprNode
import analyser.nodes.type.*
import exceptions.SemanticsException
import org.antlr.v4.runtime.ParserRuleContext

data class BinOpNode(
    val operator: BinaryOperator,
    val firstExpr: ExprNode,
    val secondExpr: ExprNode,
    override val ctx: ParserRuleContext?
) : ExprNode {
    override var type: Type = operator.returnType
    override lateinit var st: SymbolTable
    override lateinit var funTable: SymbolTable

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        this.st = st
        this.funTable = funTable
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