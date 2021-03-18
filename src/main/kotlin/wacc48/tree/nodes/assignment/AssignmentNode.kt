package wacc48.tree.nodes.assignment

import org.antlr.v4.runtime.ParserRuleContext
import wacc48.analyser.exceptions.Issue
import wacc48.analyser.exceptions.addSemantic
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.ASTNode
import wacc48.tree.nodes.function.FuncCallNode
import wacc48.tree.nodes.function.FuncNode
import wacc48.tree.nodes.statement.StatNode

data class AssignmentNode(
    val name: LHSNode,
    var value: RHSNode,
    val ctx: ParserRuleContext?,
) : StatNode {
    override val children: List<ASTNode>
        get() = listOf(name, value)

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>,
        issues: MutableList<Issue>
    ) {
        name.mode = AccessMode.ASSIGN
        name.validate(st, funTable, issues)
        value.validate(st, funTable, issues)

        //Assume type matches if this in a function body in the wacc48.shell
        if (!(value is FuncCallNode && value.inShellAndFuncNodeCtx)) {
            if (name.type != value.type) {
                println(ctx?.text)
                issues.addSemantic(
                    "Attempt to assign ${value.type} to ${name.type}",
                    ctx
                )
            }
        }
    }

    override fun <T> acceptVisitor(visitor: ASTVisitor<T>): T {
        return visitor.visitAssignment(this)
    }

}