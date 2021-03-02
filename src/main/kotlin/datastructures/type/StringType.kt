package datastructures.nodes.type

object StringType : Type {
    override val reserveStackSize: Int = 4


    override fun toString(): String {
        return "String"
    }
}