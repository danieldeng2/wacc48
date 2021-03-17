package wacc48.tree.nodes.statement

import org.antlr.v4.runtime.ParserRuleContext
import wacc48.analyser.exceptions.Issue
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.expr.ExprNode
import wacc48.tree.nodes.function.FuncNode

data class PrintNode(
    var value: ExprNode,
    val returnAfterPrint: Boolean = false,
    val ctx: ParserRuleContext?,
) : StatNode {


    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>,
        issues: MutableList<Issue>
    ) {
        value.validate(st, funTable, issues)
    }

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitPrint(this)
    }
}