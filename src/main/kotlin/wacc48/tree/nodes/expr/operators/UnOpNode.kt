package wacc48.tree.nodes.expr.operators

import org.antlr.v4.runtime.ParserRuleContext
import wacc48.analyser.exceptions.Issue
import wacc48.analyser.exceptions.addSyntax
import wacc48.shell.MemoryTable
import wacc48.shell.detectIntegerOverflow
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.expr.ArrayLiteral
import wacc48.tree.nodes.expr.BoolLiteral
import wacc48.tree.nodes.expr.CharLiteral
import wacc48.tree.nodes.expr.ExprNode
import wacc48.tree.nodes.expr.IntLiteral
import wacc48.tree.nodes.expr.Literal
import wacc48.tree.nodes.expr.StringLiteral
import wacc48.tree.nodes.function.FuncNode
import wacc48.tree.type.ArrayType
import wacc48.tree.type.BoolType
import wacc48.tree.type.CharType
import wacc48.tree.type.IntType
import wacc48.tree.type.StringType
import wacc48.tree.type.Type
import wacc48.tree.type.VoidType

data class UnOpNode(
    val operator: UnaryOperator,
    var expr: ExprNode,
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
        return IntLiteral(-intExpr.value, false, null)
    }

    private fun reduceNegateToLiteral(mt: MemoryTable?): Literal {
        val boolExpr = expr.reduceToLiteral(mt) as BoolLiteral
        return BoolLiteral(!boolExpr.value, null)
    }

    private fun reduceLenToLiteral(mt: MemoryTable?): Literal {
        return if (expr.type is StringType) {
            val strExpr = expr.reduceToLiteral(mt) as StringLiteral
            IntLiteral(strExpr.value.length, false, null)
        } else { //Array type
            val arrExpr = expr.reduceToLiteral(mt) as ArrayLiteral
            IntLiteral(arrExpr.values.size, false, null)
        }
    }

    private fun reduceOrdToLiteral(mt: MemoryTable?): Literal {
        val charExpr = expr.reduceToLiteral(mt) as CharLiteral
        return IntLiteral(charExpr.value.toInt(), false, null)
    }

    private fun reduceChrToLiteral(mt: MemoryTable?): Literal {
        val intExpr = expr.reduceToLiteral(mt) as IntLiteral
        return CharLiteral(intExpr.value.toChar(), null)
    }

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>,
        issues: MutableList<Issue>
    ) {

        expr.validate(st, funTable, issues)

        if (expr.type !in operator.expectedExprTypes)
            issues.addSyntax(
                "Expression type for $operator " +
                        "does not match required type $type",
                ctx
            )
    }


    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitUnOp(this)
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