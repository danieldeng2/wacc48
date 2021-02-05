package analyser.nodes.expr.operators

import analyser.SymbolTable
import analyser.nodes.expr.ExprNode
import analyser.nodes.type.*
import exceptions.SemanticsException
import kotlin.math.exp

open class UnOpNode(
    val operator: UnaryOperator,
    val expr: ExprNode,
) : ExprNode {
    override var type: Type = operator.returnType

    override fun validate(st: SymbolTable) {
        expr.validate(st)

        if (expr.type !in operator.expectedExprTypes)
            throw SemanticsException(
                "Expression type for $operator " +
                        "does not match required type $type"
            )
    }
}

enum class UnaryOperator(
    val repr: String, val expectedExprTypes: List<Type>, val returnType: Type
) {
    PLUS("+", listOf(IntType), IntType),
    MINUS("-", listOf(IntType), IntType),
    NEGATE("!", listOf(BoolType), BoolType),
    LEN("len", listOf(StringType, ArrayType(VoidType)), IntType),
    ORD("ord", listOf(CharType), IntType),
    CHR("chr", listOf(IntType), CharType);

    companion object {
        fun lookupRepresentation(string: String) =
            values().firstOrNull { it.repr == string }

    }
}