package analyser.nodes.type

import analyser.SymbolTable

object CharType : Type {
    override fun validate(st: SymbolTable, funTable: SymbolTable) {
    }

    override fun toString(): String {
        return "Char"
    }
}