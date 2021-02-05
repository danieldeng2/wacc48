package analyser.nodes.type

import analyser.SymbolTable


data class PairType(var firstType: Type, var secondType: Type) :
    GenericPair {

    override fun validate(st: SymbolTable) {
    }

    override fun equals(other: Any?): Boolean {
        return other is GenericPair
    }

    override fun hashCode(): Int {
        return 1
    }


}