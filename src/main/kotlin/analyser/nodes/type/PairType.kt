package analyser.nodes.type

import analyser.SymbolTable

/**
 *
 *  'null' means generic pair
 */

class PairType(var firstType: Type? = null, var secondType: Type? = null) :
    Type {

    override fun isValid(st: SymbolTable): Boolean = true
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PairType

        if (firstType != other.firstType &&
            !(firstType is PairType && other.firstType is PairType)
        )
            return false

        if (secondType != other.secondType &&
            !(secondType is PairType && other.secondType is PairType)
        )
            return false

        return true
    }

    override fun hashCode(): Int {
        var result = firstType.hashCode()
        result = 31 * result + secondType.hashCode()
        return result
    }


}