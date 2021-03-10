package generator.instructions.operands

class MemAddr(
    val reg: Register,
    val offset: NumOp = NumOp(0),
    val updateBaseReg: Boolean = false
) : LoadableOp {

    override fun toArm(): String {
        val str = when (offset.value) {
            0 -> "[${reg.toArm()}]"
            else -> "[${reg.toArm()}, ${offset.toArm()}]"
        }

        return str + if (updateBaseReg) "!" else ""
    }

}
