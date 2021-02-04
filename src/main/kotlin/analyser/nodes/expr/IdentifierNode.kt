package analyser.nodes.expr

import analyser.SymbolTable
import analyser.nodes.assignment.LHSNode
import analyser.nodes.type.Typable
import analyser.nodes.type.Type
import analyser.nodes.type.VoidType
import exceptions.SemanticsException

data class IdentifierNode(
    val name: String,
) : LHSNode, ExprNode {
    override var type: Type = VoidType

    override fun validate(st: SymbolTable) {
        if (!st.containsInAnyScope(name))
            throw SemanticsException("Unknown identifier $name")

        val assignedNode = st[name]
        if (assignedNode !is Typable)
            throw SemanticsException("Unknown type $name")

        type = assignedNode.type
    }
}