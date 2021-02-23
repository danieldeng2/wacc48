package generator.armInstructions.operands

class MemAddr(val reg: Register, val offset: NumOp) {
    override fun toString() = when (offset.value) {
        0 -> "[$reg]"
        else -> "[$reg, $offset]"
    }

}
