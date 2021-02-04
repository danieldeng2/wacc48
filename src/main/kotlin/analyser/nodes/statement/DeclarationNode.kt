package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.assignment.RHSNode
import analyser.nodes.function.ParamNode
import exceptions.SemanticsException

data class DeclarationNode(
    private val name: ParamNode,
    private val value: RHSNode
) : StatNode {

    override fun validate(st: SymbolTable) {
        name.validate(st)
        value.validate(st)

        if (value.type != name.type)
            throw SemanticsException("Type mismatch in declaration $name")
        if (st.containsInCurrentScope(name.text))
            throw SemanticsException("Illegal re-declaration of $name")
        st.add(name.text, value)
    }
}
