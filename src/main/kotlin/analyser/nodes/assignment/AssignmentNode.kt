package analyser.nodes.assignment

import analyser.SymbolTable
import analyser.nodes.statement.StatNode
import exceptions.SemanticsException
import org.antlr.v4.runtime.ParserRuleContext

data class AssignmentNode(
    val name: LHSNode,
    val value: RHSNode,
    override val ctx: ParserRuleContext?
) : StatNode {
    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        name.validate(st, funTable)
        value.validate(st, funTable)

        if (name.type != value.type)
            throw SemanticsException("Attempt to assign ${value.type} to ${name.type}", ctx)
    }
}