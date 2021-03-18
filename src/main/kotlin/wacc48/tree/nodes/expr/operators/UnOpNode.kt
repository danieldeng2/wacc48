package wacc48.tree.nodes.expr.operators

import org.antlr.v4.runtime.ParserRuleContext
import wacc48.analyser.exceptions.Issue
import wacc48.analyser.exceptions.addSyntax
import wacc48.shell.MemoryTable
import wacc48.shell.detectIntegerOverflow
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.ASTNode
import wacc48.tree.nodes.expr.*
import wacc48.tree.nodes.expr.operators.operation.unary.UnaryOperation
import wacc48.tree.nodes.function.FuncNode
import wacc48.tree.type.*

data class UnOpNode(
    val operation: UnaryOperation,
    var expr: ExprNode,
    val ctx: ParserRuleContext?,
) : ExprNode {
    override var type: Type = operation.returnType

    override fun reduceToLiteral(mt: MemoryTable?): Literal =
        operation.reduceToLiteral(mt, expr)

    override val children: List<ASTNode>
        get() = listOf(expr)

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>,
        issues: MutableList<Issue>
    ) {
        expr.validate(st, funTable, issues)

        if (expr.type !in operation.expectedExprTypes)
            issues.addSyntax(
                "Expression type for ${operation.repr} " +
                        "does not match required type $type",
                ctx
            )
    }

    override fun <T> acceptVisitor(visitor: ASTVisitor<T>): T {
        return visitor.visitUnOp(this)
    }
}