package datastructures.type

object EmptyPair : GenericPair {
    override val reserveStackSize: Int = 4

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

