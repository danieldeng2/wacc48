package analyser.nodes.type

import analyser.SymbolTable

object StringType : Type {

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
    }

    override fun toString(): String {
        return "String"
    }
}