package generator.instructions.operands

class MemAddr(
    val reg: Register,
    val offset: NumOp = NumOp(0),
    val updateBaseReg: Boolean = false
) : LoadableOp {

    override fun tox86() = when (offset.value) {
        0 -> "[${reg.tox86()}]"
        else -> "[${reg.tox86()} + ${offset.tox86()}]"
    }

    override fun toArm(): String {
        val str = when (offset.value) {
            0 -> "[${reg.toArm()}]"
            else -> "[${reg.toArm()}, ${offset.toArm()}]"
        }

        return str + if (updateBaseReg) "!" else ""
    }

}
