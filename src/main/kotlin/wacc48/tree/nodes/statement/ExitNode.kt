package wacc48.tree.nodes.statement

import org.antlr.v4.runtime.ParserRuleContext
import wacc48.analyser.exceptions.Issue
import wacc48.analyser.exceptions.addSemantic
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.ASTNode
import wacc48.tree.nodes.expr.ExprNode
import wacc48.tree.nodes.function.FuncNode
import wacc48.tree.type.IntType

data class ExitNode(
    var expr: ExprNode,
    val ctx: ParserRuleContext?
) : StatNode {

    override val children: List<ASTNode>
        get() = listOf(expr)

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>,
        issues: MutableList<Issue>
    ) {

        expr.validate(st, funTable, issues)
        if (expr.type != IntType)
            issues.addSemantic(
                "Exit must take integer as input, got ${expr.type} instead",
                ctx
            )
    }

    override fun <T> acceptVisitor(visitor: ASTVisitor<T>): T {
        return visitor.visitExit(this)
    }

}
