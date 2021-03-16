package wacc48.tree.nodes.function

import org.antlr.v4.runtime.ParserRuleContext
import wacc48.analyser.exceptions.Issue
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.ASTNode
import wacc48.tree.nodes.expr.ExprNode

data class ArgListNode(
    var args: List<ExprNode>,
    val ctx: ParserRuleContext?
) : ASTNode {
    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>,
        issues: MutableList<Issue>
    ) {
        args.forEach {
            it.validate(st, funTable, issues)
        }
    }

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitArgList(this)
    }
}