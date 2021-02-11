package analyser.nodes.type

import analyser.SymbolTable
import org.antlr.v4.runtime.ParserRuleContext

data class ArrayType(val elementType: Type, override val ctx: ParserRuleContext?) : Type {

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
    }

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
}