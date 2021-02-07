package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.assignment.RHSNode
import analyser.nodes.function.ParamNode
import exceptions.SemanticsException

data class DeclarationNode(
    private val name: ParamNode,
    private val value: RHSNode
) : StatNode {

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        name.validate(st, funTable)
        value.validate(st, funTable)

        if (value.type != name.type)
            throw SemanticsException(".*", null)
    }
}
