package analyser.nodes.type

import analyser.SymbolTable

data class ArrayType(val elementType: Type) : Type {

    override fun validate(st: SymbolTable) {
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ArrayType

        if (elementType != other.elementType) return false

        return true
    }

    override fun hashCode(): Int {
        return elementType.hashCode()
    }
}