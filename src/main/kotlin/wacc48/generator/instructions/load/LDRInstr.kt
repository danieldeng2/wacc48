package wacc48.generator.instructions.load

import wacc48.generator.instructions.Instruction
import wacc48.generator.instructions.operands.LoadableOp
import wacc48.generator.instructions.operands.Register

class LDRInstr(
    private val reg: Register,
    private val op: LoadableOp,
) : Instruction {

    override fun tox86() = listOf("\tmov ${reg.tox86()}, ${op.tox86()}")

    override fun toArm() = "\tLDR ${reg.toArm()}, ${op.toArm()}"
}