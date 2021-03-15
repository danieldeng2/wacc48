package generator.instructions.load

import generator.instructions.Instruction
import generator.instructions.operands.LoadableOp
import generator.instructions.operands.Register

class LDRInstr(
    private val reg: Register,
    private val op: LoadableOp,
) : Instruction {

    override fun tox86() = listOf("\tmov ${reg.tox86()}, ${op.tox86()}")

    override fun toArm() = "\tLDR ${reg.toArm()}, ${op.toArm()}"
}