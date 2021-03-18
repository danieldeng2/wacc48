package wacc48.tree.nodes.statement

import org.antlr.v4.runtime.ParserRuleContext
import wacc48.analyser.exceptions.Issue
import wacc48.analyser.exceptions.addSemantic

import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.ASTNode
import wacc48.tree.nodes.assignment.RHSNode
import wacc48.tree.nodes.function.FuncCallNode
import wacc48.tree.nodes.function.FuncNode
import wacc48.tree.nodes.function.ParamNode

data class DeclarationNode(
    val name: ParamNode,
    var value: RHSNode,
    val ctx: ParserRuleContext?
) : StatNode {
    lateinit var st: SymbolTable

    override val children: List<ASTNode>
        get() = listOf(name, value)

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>,
        issues: MutableList<Issue>
    ) {
        this.st = st
        name.validate(st, funTable, issues)
        value.validate(st, funTable, issues)

        //Assume type matches if this in a function body in the wacc48.shell
        if (!(value is FuncCallNode && (value as FuncCallNode).inShellAndFuncNodeCtx)) {
            if (value.type != name.type)
                issues.addSemantic(
                    "Type mismatch in declaration of ${name.text}, expected ${name.type}, actual ${value.type}",
                    ctx
                )
        }
    }

    override fun <T> acceptVisitor(visitor: ASTVisitor<T>): T {
        return visitor.visitDeclaration(this)
    }


}

