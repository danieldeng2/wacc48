package analyser.nodes.type

import analyser.SymbolTable

object BoolType : Type {
    override fun isValid(st: SymbolTable) = true
}