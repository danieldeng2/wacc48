package wacc48.tree.nodes.expr.operators

import org.antlr.v4.runtime.ParserRuleContext
import wacc48.analyser.exceptions.Issue
import wacc48.analyser.exceptions.addSemantic
import wacc48.shell.*
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.ASTNode
import wacc48.tree.nodes.expr.*
import wacc48.tree.nodes.expr.operators.operation.binary.BinaryOperation
import wacc48.tree.nodes.function.FuncNode
import wacc48.tree.type.*

data class BinOpNode(
    val operation: BinaryOperation,
    var firstExpr: ExprNode,
    var secondExpr: ExprNode,
    val ctx: ParserRuleContext?
) : ExprNode {
    override var type: Type = operation.returnType

    override fun reduceToLiteral(mt: MemoryTable?): Literal =
        operation.reduceToLiteral(mt, firstExpr, secondExpr)

    override val children: List<ASTNode>
        get() = listOf(firstExpr, secondExpr)

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>,
        issues: MutableList<Issue>
    ) {
        firstExpr.validate(st, funTable, issues)
        secondExpr.validate(st, funTable, issues)
        val expected = operation.expectedExprTypes

        if (expected.isNotEmpty()) {
            if (firstExpr.type !in expected)
                issues.addSemantic(
                    "Type-mismatched on operator ${operation.repr}: arg 1 has type " +
                            "${firstExpr.type}, required 1 of type(s) $expected",
                    ctx
                )
        }

        if (firstExpr.type != secondExpr.type)
            issues.addSemantic(
                "Type-mismatched on operator ${operation.repr}: arg 1 has type " +
                        "${firstExpr.type}, arg 2 of type(s) ${secondExpr.type}",
                ctx
            )
    }

    override fun <T> acceptVisitor(visitor: ASTVisitor<T>): T {
        return visitor.visitBinOp(this)
    }

}
