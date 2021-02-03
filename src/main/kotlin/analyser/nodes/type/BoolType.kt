package analyser.nodes.type

import analyser.SymbolTable

object BoolType : Type {
    override fun validate(st: SymbolTable) {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "Bool"
    }
}