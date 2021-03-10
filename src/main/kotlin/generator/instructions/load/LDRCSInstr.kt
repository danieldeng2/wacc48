package generator.instructions.load

import generator.instructions.Instruction
import generator.instructions.operands.LoadableOp
import generator.instructions.operands.Register

class LDRCSInstr(val rd: Register, val op: LoadableOp) :
    Instruction {
    override fun toArm() = "\tLDRCS ${rd.toArm()}, ${op.toArm()}"
}
