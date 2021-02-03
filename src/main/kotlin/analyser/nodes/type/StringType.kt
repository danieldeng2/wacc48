package analyser.nodes.type

import analyser.SymbolTable

object StringType : Type {

    override fun isValid(st: SymbolTable) = true
}