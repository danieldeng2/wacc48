package generator.armInstructions.operands

class MemAddr(val reg: Register, val offset: NumOp) {
    override fun toString() = when (offset.value) {
        0 -> "[${reg.repr}]"
        else -> "[${reg.repr}, $offset]"
    }

}
