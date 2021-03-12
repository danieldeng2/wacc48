package tree.nodes.expr.operators

import analyser.exceptions.SyntaxException
import tree.SymbolTable
import tree.nodes.function.FuncNode
import tree.type.*
import generator.translator.CodeGeneratorVisitor
import org.antlr.v4.runtime.ParserRuleContext
import shell.CodeEvaluatorVisitor
import shell.MemoryTable
import shell.detectIntegerOverflow
import tree.nodes.expr.*

data class UnOpNode(
    val operator: UnaryOperator,
    val expr: ExprNode,
    val ctx: ParserRuleContext?,
) : ExprNode {
    override var type: Type = operator.returnType
    override fun reduceToLiteral(mt: MemoryTable?): Literal =
        when (operator) {
            UnaryOperator.MINUS -> reduceMinusToLiteral(mt)
            UnaryOperator.NEGATE -> reduceNegateToLiteral(mt)
            UnaryOperator.LEN -> reduceLenToLiteral(mt)
            UnaryOperator.ORD -> reduceOrdToLiteral(mt)
            UnaryOperator.CHR -> reduceChrToLiteral(mt)
        }

    private fun reduceMinusToLiteral(mt: MemoryTable?): Literal {
        val intExpr = expr.reduceToLiteral(mt) as IntLiteral
        detectIntegerOverflow(0, intExpr.value, BinaryOperator.MINUS)
        return IntLiteral(-intExpr.value, null)
    }

    private fun reduceNegateToLiteral(mt: MemoryTable?): Literal {
        val boolExpr = expr.reduceToLiteral(mt) as BoolLiteral
        return BoolLiteral(!boolExpr.value, null)
    }

    private fun reduceLenToLiteral(mt: MemoryTable?): Literal {
        if (expr.type is StringType) {
            val strExpr = expr.reduceToLiteral(mt) as StringLiteral
            return IntLiteral(strExpr.value.length, null)
        } else { //Array type
            val arrExpr = expr.reduceToLiteral(mt) as ArrayLiteral
            return IntLiteral(arrExpr.values.size, null)
        }
    }

    private fun reduceOrdToLiteral(mt: MemoryTable?): Literal {
        val charExpr = expr.reduceToLiteral(mt) as CharLiteral
        return IntLiteral(charExpr.value.toInt(), null)
    }

    private fun reduceChrToLiteral(mt: MemoryTable?): Literal {
        val intExpr = expr.reduceToLiteral(mt) as IntLiteral
        return CharLiteral(intExpr.value.toChar(), null)
    }

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

    override fun acceptCodeEvalVisitor(visitor: CodeEvaluatorVisitor): Literal? {
        return visitor.translateUnOp(this)
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