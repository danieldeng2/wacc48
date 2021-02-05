package analyser.nodes.type

import analyser.SymbolTable

object BoolType : Type {
    override fun validate(st: SymbolTable, funTable: SymbolTable) {
    }

    override fun toString(): String {
        return "Bool"
    }
}