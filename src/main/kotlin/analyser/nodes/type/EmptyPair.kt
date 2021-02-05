package analyser.nodes.type

import analyser.SymbolTable

object EmptyPair : GenericPair {
    override fun validate(st: SymbolTable) {
    }

    override fun equals(other: Any?): Boolean {
        return other is GenericPair
    }

    override fun hashCode(): Int {
        return 1
    }

    override fun toString(): String {
        return "Pair"
    }
}

