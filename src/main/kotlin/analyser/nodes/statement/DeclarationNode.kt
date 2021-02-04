package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.assignment.RHSNode
import analyser.nodes.type.Typable
import analyser.nodes.type.Type
import exceptions.SemanticsException

data class DeclarationNode(
    override var type: Type,
    private val name: String,
    private val value: RHSNode
) : StatNode, Typable {

    override fun validate(st: SymbolTable) {
        if (value.type != type)
            throw SemanticsException("Type mismatch in declaration $name")
        value.validate(st)

        st.add(name, value)
    }
}
