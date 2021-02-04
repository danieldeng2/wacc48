package analyser.nodes.type

import analyser.SymbolTable

object VoidType : Type {
    override fun validate(st: SymbolTable) {
    }

    override fun toString(): String {
        return "Void"
    }

    override fun equals(other: Any?): Boolean {
        return other is Type
    }

    override fun hashCode(): Int {
        return 0
    }
}