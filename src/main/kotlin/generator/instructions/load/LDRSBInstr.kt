package generator.instructions.load

import generator.instructions.Instruction
import generator.instructions.operands.LoadableOp
import generator.instructions.operands.Register

class LDRSBInstr(
    private val reg: Register,
    private val op: LoadableOp
) : Instruction {

    override fun tox86() =
        listOf("\tmovsx ${reg.tox86()}, byte ${op.tox86()}")

    override fun toArm() = "\tLDRSB ${reg.toArm()}, ${op.toArm()}"
}