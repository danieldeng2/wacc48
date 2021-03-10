package generator.instructions.compare

import generator.instructions.Instruction
import generator.instructions.operands.LoadableOp
import generator.instructions.operands.Register

class CMPInstr(val rd: Register, val op: LoadableOp) : Instruction {
    override fun toArm() = "\tCMP ${rd.toArm()}, ${op.toArm()}"
}
