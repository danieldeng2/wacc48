package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.assignment.LHSNode
import analyser.nodes.type.PairType
import analyser.nodes.type.Typable
import analyser.nodes.type.Type
import exceptions.SemanticsException

data class IdentifierNode(
    val name: String,
) : LHSNode, StatNode {

    override var type: Type = PairType()

    override fun validate(st: SymbolTable) {
        if (name !in st)
            throw SemanticsException("Unknown identifier $name")

        val assignedNode = st[name]
        if (assignedNode !is Typable) {
            throw SemanticsException("Unknown type $name")
        }

        type = assignedNode.type
    }
}