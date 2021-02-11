package analyser.nodes.type

import analyser.SymbolTable
import org.antlr.v4.runtime.ParserRuleContext


data class PairType(var firstType: Type, var secondType: Type, override val ctx: ParserRuleContext?) :
    GenericPair {

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
    }

    override fun equals(other: Any?): Boolean {
        return other is GenericPair
    }

    override fun hashCode(): Int {
        return 1
    }


}