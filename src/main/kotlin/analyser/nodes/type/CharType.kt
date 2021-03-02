package analyser.nodes.type

object CharType : Type {
    override val reserveStackSize: Int = 1

    override fun toString(): String {
        return "Char"
    }

}