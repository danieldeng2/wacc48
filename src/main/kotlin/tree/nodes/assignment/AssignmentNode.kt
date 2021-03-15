package tree.nodes.assignment

import analyser.exceptions.SemanticsException
import tree.SymbolTable
import tree.nodes.function.FuncNode
import tree.nodes.statement.StatNode
import org.antlr.v4.runtime.ParserRuleContext
import tree.ASTVisitor

data class AssignmentNode(
    val name: LHSNode,
    var value: RHSNode,
    val ctx: ParserRuleContext?
) : StatNode {

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        name.mode = AccessMode.ASSIGN
        name.validate(st, funTable)
        value.validate(st, funTable)

        if (name.type != value.type)
            throw SemanticsException(
                "Attempt to assign ${value.type} to ${name.type}",
                ctx
            )
    }

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitAssignment(this)
    }
}