package generator.instructions.operands

class MemAddr(
    val reg: Register,
    val offset: NumOp = NumOp(0),
    val updateBaseReg: Boolean = false
) : LoadableOp {
    override fun toString(): String {
        val str = when (offset.value) {
            0 -> "[${reg.repr}]"
            else -> "[${reg.repr}, $offset]"
        }

        return str + if (updateBaseReg) "!" else ""
    }

}
