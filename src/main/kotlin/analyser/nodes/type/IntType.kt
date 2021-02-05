package analyser.nodes.type

import analyser.SymbolTable

object IntType : Type {

    const val min = Int.MIN_VALUE
    const val max = Int.MAX_VALUE

    override fun validate(st: SymbolTable, funTable: SymbolTable) {}

    override fun toString(): String {
        return "Int"
    }
}