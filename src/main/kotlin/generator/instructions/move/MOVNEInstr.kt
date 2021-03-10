package generator.instructions.move

import generator.instructions.Instruction
import generator.instructions.operands.ImmOp
import generator.instructions.operands.Register

class MOVNEInstr(val rd: Register, val op: ImmOp) :
    Instruction {

    override fun toArm() = "\tMOVNE ${rd.toArm()}, ${op.toArm()}"
}
