package generator.instructions.load

import generator.instructions.Instruction
import generator.instructions.operands.LoadableOp
import generator.instructions.operands.Register

class LDRSBInstr(
    private val reg: Register,
    private val op: LoadableOp
) : Instruction {

    override fun tox86() =
        listOf("\tmov ${reg.x86AddressByte(1)}, ${op.tox86()}")

    override fun toArm() = "\tLDRSB ${reg.toArm()}, ${op.toArm()}"
}