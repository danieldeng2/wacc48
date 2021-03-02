package datastructures.nodes.type

object VoidType : Type {
    override val reserveStackSize: Int = 0

    override fun toString(): String {
        return "Void"
    }

    override fun equals(other: Any?): Boolean {
        return other is Type
    }

    override fun hashCode(): Int {
        return 0
    }

}