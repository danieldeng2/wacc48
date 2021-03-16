package wacc48.tree.type

import org.antlr.v4.runtime.ParserRuleContext

data class ArrayType(val elementType: Type, val ctx: ParserRuleContext?) : Type {
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