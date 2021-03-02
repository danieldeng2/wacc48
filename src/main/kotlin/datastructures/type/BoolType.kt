package datastructures.nodes.type

object BoolType : Type {
    override val reserveStackSize: Int = 1

    override fun toString(): String {
        return "Bool"
    }

}