package analyser.nodes.type

import analyser.SymbolTable

object EmptyPair : GenericPair {
    override fun validate(st: SymbolTable) {
    }

    override fun toString(): String {
        return "Pair"
    }
}

