package wacc48.generator.instructions.load

import wacc48.generator.instructions.Instruction
import wacc48.generator.instructions.operands.LoadableOp
import wacc48.generator.instructions.operands.Register

class LDRSBInstr(
    private val reg: Register,
    private val op: LoadableOp
) : Instruction {

    override fun tox86() =
        listOf("\tmovsx ${reg.tox86()}, byte ${op.tox86()}")

    override fun toArm() = "\tLDRSB ${reg.toArm()}, ${op.toArm()}"
}