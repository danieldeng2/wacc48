package tree.nodes.expr.operators

import analyser.exceptions.SemanticsException
import tree.SymbolTable
import tree.nodes.expr.ExprNode
import tree.nodes.function.FuncNode
import tree.type.BoolType
import tree.type.CharType
import tree.type.IntType
import tree.type.Type
import org.antlr.v4.runtime.ParserRuleContext
import tree.ASTVisitor

data class BinOpNode(
    val operator: BinaryOperator,
    var firstExpr: ExprNode,
    var secondExpr: ExprNode,
    val ctx: ParserRuleContext?
) : ExprNode {
    override var type: Type = operator.returnType


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

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitBinOp(this)
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