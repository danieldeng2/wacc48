package wacc48.tree.type

import org.antlr.v4.runtime.ParserRuleContext


data class PairType(
    var firstType: Type,
    var secondType: Type,
    val ctx: ParserRuleContext?
) : GenericPair {

    override val reserveStackSize: Int = 4

    override fun equals(other: Any?): Boolean {
        //return other is GenericPair
        if (this === other)
            return true
        if (other !is GenericPair)
            return false
        if (other is EmptyPair)
            return true

        other as PairType
        if (other.firstType != firstType)
            if (!(firstType is GenericPair && other.firstType is GenericPair))
                return false

        if (other.secondType != secondType)
            if (!(secondType is GenericPair && other.secondType is GenericPair))
                return false

        return true
    }

    override fun hashCode(): Int {
        return 1
    }

    override fun toString(): String =
        "Pair<$firstType , $secondType>"
}