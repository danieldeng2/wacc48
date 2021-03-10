package generator.instructions.load

import generator.instructions.Instruction
import generator.instructions.operands.LoadableOp
import generator.instructions.operands.Register

class LDRSBInstr(
    private val reg: Register,
    private val op: LoadableOp
) : Instruction {

    override fun toArm() = "\tLDRSB ${reg.toArm()}, ${op.toArm()}"
}