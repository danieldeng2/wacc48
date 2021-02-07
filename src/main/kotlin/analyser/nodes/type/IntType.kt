package analyser.nodes.type

import analyser.SymbolTable
import org.antlr.v4.runtime.ParserRuleContext

object IntType : Type {

    const val min = Int.MIN_VALUE
    const val max = Int.MAX_VALUE
    override val ctx: ParserRuleContext?
        get() = null

    override fun validate(st: SymbolTable, funTable: SymbolTable) {}

    override fun toString(): String {
        return "Int"
    }
}