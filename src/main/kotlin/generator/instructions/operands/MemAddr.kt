package generator.instructions.operands

class MemAddr(val reg: Register, val offset: NumOp) : LoadableOp {
    override fun toString() = when (offset.value) {
        0 -> "[${reg.repr}]"
        else -> "[${reg.repr}, $offset]"
    }

}
