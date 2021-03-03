 package datastructures.nodes.expr.operators

import analyser.exceptions.SemanticsException
import datastructures.SymbolTable
import datastructures.nodes.expr.ExprNode
import datastructures.nodes.function.FuncNode
import datastructures.type.BoolType
import datastructures.type.CharType
import datastructures.type.IntType
import datastructures.type.Type
import generator.translator.CodeGeneratorVisitor
import org.antlr.v4.runtime.ParserRuleContext

 data class BinOpNode(
    val operator: BinaryOperator,
    val firstExpr: ExprNode,
    val secondExpr: ExprNode,
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