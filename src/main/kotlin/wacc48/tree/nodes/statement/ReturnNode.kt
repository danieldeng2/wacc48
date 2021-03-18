package wacc48.tree.nodes.statement

import org.antlr.v4.runtime.ParserRuleContext
import wacc48.analyser.exceptions.Issue
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.ASTNode
import wacc48.tree.nodes.expr.ExprNode
import wacc48.tree.nodes.function.FuncNode

data class ReturnNode(
    var value: ExprNode,
    val ctx: ParserRuleContext?,
) : StatNode {
    lateinit var st: SymbolTable
    override val children: List<ASTNode>
        get() = listOf(value)

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>,
        issues: MutableList<Issue>
    ) {
        this.st = st
        value.validate(st, funTable, issues)
    }

    override fun <T> acceptVisitor(visitor: ASTVisitor<T>) : T {
        return visitor.visitReturn(this)
    }
}