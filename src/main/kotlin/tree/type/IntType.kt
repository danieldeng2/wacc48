package tree.type

object IntType : Type {
    const val min = Int.MIN_VALUE
    const val max = Int.MAX_VALUE

    override val reserveStackSize: Int = 4


    override fun toString(): String {
        return "Int"
    }

}