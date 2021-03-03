package tree.nodes.expr.operators

import analyser.exceptions.SyntaxException
import tree.SymbolTable
import tree.nodes.expr.ExprNode
import tree.nodes.function.FuncNode
import tree.type.*
import generator.translator.CodeGeneratorVisitor
import org.antlr.v4.runtime.ParserRuleContext

data class UnOpNode(
    val operator: UnaryOperator,
    val expr: ExprNode,
    val ctx: ParserRuleContext?,
) : ExprNode {
    override var type: Type = operator.returnType


    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {

        expr.validate(st, funTable)

        if (expr.type !in operator.expectedExprTypes)
            throw SyntaxException(
                "Expression type for $operator " +
                        "does not match required type $type"
            )
    }


    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateUnOp(this)
    }

}

enum class UnaryOperator(
    val repr: String, val expectedExprTypes: List<Type>, val returnType: Type
) {
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