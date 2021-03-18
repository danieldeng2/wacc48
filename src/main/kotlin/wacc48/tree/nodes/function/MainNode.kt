package wacc48.tree.nodes.function

import org.antlr.v4.runtime.ParserRuleContext
import wacc48.analyser.exceptions.Issue
import wacc48.analyser.exceptions.addSemantic
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.ASTNode
import wacc48.tree.nodes.statement.BeginNode
import wacc48.tree.nodes.statement.IfNode
import wacc48.tree.nodes.statement.ReturnNode
import wacc48.tree.nodes.statement.SeqNode
import wacc48.tree.nodes.statement.StatNode
import wacc48.tree.nodes.statement.WhileNode

class MainNode(
    var body: StatNode,
    val ctx: ParserRuleContext?
) : ASTNode {
    lateinit var st: SymbolTable

    override val children: List<ASTNode>
        get() = listOf(body)

    override fun validate(
        st: SymbolTable, funTable: MutableMap<String, FuncNode>,
        issues: MutableList<Issue>
    ) {
        this.st = st
        body.validate(st, funTable, issues)

        if (hasGlobalReturn(body))
            issues.addSemantic("Cannot return in global context", ctx)
    }

    override fun <T> acceptVisitor(visitor: ASTVisitor<T>): T {
        return visitor.visitMain(this)
    }

    private fun hasGlobalReturn(body: StatNode): Boolean =
        when (body) {
            is IfNode -> hasGlobalReturn(body.trueStat) || hasGlobalReturn(body.falseStat)
            is SeqNode -> body.any { hasGlobalReturn(it) }
            is WhileNode -> hasGlobalReturn(body.body)
            is BeginNode -> hasGlobalReturn(body.stat)
            is ReturnNode -> true
            else -> false
        }

}