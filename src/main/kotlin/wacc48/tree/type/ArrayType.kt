package wacc48.tree.type

data class ArrayType(val elementType: Type) : Type {
    override val reserveStackSize: Int = 4

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ArrayType

        if (elementType != VoidType && other.elementType != VoidType && elementType != other.elementType) return false

        return true
    }

    override fun hashCode(): Int {
        return elementType.hashCode()
    }

    override fun toString(): String {
        return "${elementType}[]"
    }
}