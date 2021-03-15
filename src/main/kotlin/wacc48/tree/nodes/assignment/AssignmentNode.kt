package wacc48.tree.nodes.assignment

import wacc48.analyser.exceptions.SemanticsException
import org.antlr.v4.runtime.ParserRuleContext
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.function.FuncCallNode
import wacc48.tree.nodes.function.FuncNode
import wacc48.tree.nodes.statement.StatNode

data class AssignmentNode(
    val name: LHSNode,
    val value: RHSNode,
    val ctx: ParserRuleContext?,
) : StatNode {

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        name.mode = AccessMode.ASSIGN
        name.validate(st, funTable)
        value.validate(st, funTable)

        //Assume type matches if this in a function body in the wacc48.shell
        if (!(value is FuncCallNode && value.inShellAndFuncNodeCtx)) {
            if (name.type != value.type) {
                println(ctx?.text)
                throw SemanticsException(
                    "Attempt to assign ${value.type} to ${name.type}",
                    ctx
                )
            }
        }
    }

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitAssignment(this)
    }

}