package analyser.nodes.type

import analyser.SymbolTable

class ArrayType(val elementType: Type) : Type {

    override fun isValid(st: SymbolTable): Boolean = true

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