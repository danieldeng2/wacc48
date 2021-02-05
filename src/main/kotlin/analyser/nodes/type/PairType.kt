package analyser.nodes.type

import analyser.SymbolTable


data class PairType(var firstType: Type, var secondType: Type) :
    GenericPair {

    override fun validate(st: SymbolTable) {
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PairType

        if (
            firstType != other.firstType &&
            !(firstType is GenericPair && other.firstType is GenericPair)
        )
            return false


        if (
            secondType != other.secondType &&
            !(secondType is GenericPair && other.secondType is GenericPair)
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