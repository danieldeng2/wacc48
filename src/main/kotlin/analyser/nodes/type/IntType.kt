package analyser.nodes.type

import analyser.SymbolTable
import kotlin.math.pow

object IntType : Type {

    const val min = Int.MIN_VALUE
    const val max = Int.MAX_VALUE

    override fun validate(st: SymbolTable) {}

    override fun toString(): String {
        return "Int"
    }
}