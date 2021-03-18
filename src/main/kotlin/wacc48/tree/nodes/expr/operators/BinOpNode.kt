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
    var firstExpr: ExprNode,
    var secondExpr: ExprNode,
    val ctx: ParserRuleContext?
) : ExprNode {
    override var type: Type = operation.returnType

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