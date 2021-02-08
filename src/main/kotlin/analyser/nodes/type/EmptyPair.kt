package analyser.nodes.type

import analyser.SymbolTable
import org.antlr.v4.runtime.ParserRuleContext

object EmptyPair : GenericPair {
    override val ctx: ParserRuleContext? = null

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
    }

    override fun equals(other: Any?): Boolean {
        return other is GenericPair
    }

    override fun hashCode(): Int {
        return 1
    }

    override fun toString(): String {
        return "Pair"
    }
}

