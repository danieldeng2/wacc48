package analyser.nodes.type

import analyser.SymbolTable

object CharType : Type {
    val min: Int = 0
    val max: Int = 255
    override fun validate(st: SymbolTable) {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "Char"
    }
}