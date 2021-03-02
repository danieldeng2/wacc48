package datastructures.nodes.type

import org.antlr.v4.runtime.ParserRuleContext


data class PairType(
    var firstType: Type,
    var secondType: Type,
    val ctx: ParserRuleContext?
) : GenericPair {


    override val reserveStackSize: Int = 4

    override fun equals(other: Any?): Boolean {
        return other is GenericPair
    }

    override fun hashCode(): Int {
        return 1
    }

}