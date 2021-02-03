package analyser.nodes.function

import analyser.SymbolTable
import analyser.nodes.type.Type
import analyser.nodes.ASTNode
import exceptions.SemanticsException

data class ParamNode(val type: Type, val text: String) : ASTNode {
    override fun validate(st: SymbolTable) {
        if (text in st)
            throw SemanticsException("Illegal re-declaration of parameter $text")
        st.add(text, this)
    }
}