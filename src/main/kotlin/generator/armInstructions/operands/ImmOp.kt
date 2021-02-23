package generator.armInstructions.operands

class ImmOp(val value: Int) : LoadableOp {
    override fun toString() = "=$value"
}