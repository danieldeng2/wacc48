package analyser.nodes.assignment

import analyser.SymbolTable
import analyser.nodes.statement.StatNode
import exceptions.SemanticsException

data class AssignmentNode(
    val name: LHSNode,
    val value: RHSNode
) : StatNode {
    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        name.validate(st, funTable)
        value.validate(st, funTable)

        if (name.type != value.type)
            throw SemanticsException("Attempt to assign ${value.type} to ${name.type}")
    }
}