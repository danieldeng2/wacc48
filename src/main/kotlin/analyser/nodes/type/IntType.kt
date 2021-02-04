package analyser.nodes.type

import analyser.SymbolTable
import kotlin.math.pow

object IntType : Type {
    private const val minus2: Double = -2.0
    private const val two: Double = 2.0
    val min: Int = minus2.pow(31).toInt()
    val max: Int = two.pow(31).toInt() - 1

    override fun validate(st: SymbolTable) {
    }

    override fun toString(): String {
        return "Int"
    }
}