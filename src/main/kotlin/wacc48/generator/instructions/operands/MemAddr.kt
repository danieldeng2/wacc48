package wacc48.generator.instructions.operands

import wacc48.generator.translator.ArmConstants.NUM_BYTE_ADDRESS

interface MemoryReference : LoadableOp {
    val reg: Register
    val offset: NumOp
    val updateBaseReg: Boolean
}

class MemAddr(
    override val reg: Register,
    override val offset: NumOp = NumOp(0),
    override val updateBaseReg: Boolean = false
) : MemoryReference {

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

class ArgumentAddr(
    override val reg: Register,
    override val offset: NumOp = NumOp(0),
    override val updateBaseReg: Boolean = false
) : MemoryReference {

    override fun tox86(): String {
        val actualOffset = offset.value + 2 * NUM_BYTE_ADDRESS
        val offsetOp = NumOp(actualOffset)

        return "[${reg.tox86()} + ${offsetOp.tox86()}]"
    }

    override fun toArm(): String {
        val actualOffset = offset.value + NUM_BYTE_ADDRESS
        val offsetOp = NumOp(actualOffset)

        val str = "[${reg.toArm()}, ${offsetOp.toArm()}]"

        return str + if (updateBaseReg) "!" else ""
    }
}
