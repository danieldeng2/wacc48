package analyser.nodes.expr.operators

import analyser.SymbolTable
import analyser.nodes.expr.ExprNode
import analyser.nodes.type.*
import exceptions.SyntaxException
import org.antlr.v4.runtime.ParserRuleContext

data class UnOpNode(
    val operator: UnaryOperator,
    val expr: ExprNode,
    override val ctx: ParserRuleContext?,
) : ExprNode {
    override var type: Type = operator.returnType
    override lateinit var st: SymbolTable
    override lateinit var funTable: SymbolTable

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        this.st = st
        this.funTable = funTable
        expr.validate(st, funTable)

        if (expr.type !in operator.expectedExprTypes)
            throw SyntaxException(
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
    LEN("len", listOf(StringType, ArrayType(VoidType, null)), IntType),
    ORD("ord", listOf(CharType), IntType),
    CHR("chr", listOf(IntType), CharType);

    companion object {
        fun lookupRepresentation(string: String) =
            values().firstOrNull { it.repr == string }
    }
}