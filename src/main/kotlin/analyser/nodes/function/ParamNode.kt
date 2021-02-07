package analyser.nodes.function

import analyser.SymbolTable
import analyser.nodes.type.Type
import analyser.nodes.ASTNode
import analyser.nodes.type.Typable
import exceptions.SemanticsException

data class ParamNode(
    override var type: Type,
    val text: String
) : ASTNode, Typable {
    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        if (st.containsInCurrentScope(text))
            throw SemanticsException(".*", null)
        st.add(text, this)
    }
}