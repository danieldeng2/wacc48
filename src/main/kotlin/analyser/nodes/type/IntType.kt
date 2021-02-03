package analyser.nodes.type

import analyser.SymbolTable
import kotlin.math.pow

object IntType : Type {
    private val minus2: Double = -2.0
    private val two: Double = 2.0
    private val min: Int = minus2.pow(31).toInt()
    private val max: Int = two.pow(31).toInt() - 1

    override fun isValid(st: SymbolTable): Boolean = true
}