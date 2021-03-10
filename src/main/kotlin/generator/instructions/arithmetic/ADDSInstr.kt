package generator.instructions.arithmetic

import generator.instructions.Instruction
import generator.instructions.operands.LoadableOp
import generator.instructions.operands.Register

class ADDSInstr(val rd: Register, val rn: Register, val imm: LoadableOp) :
    Instruction {

    override fun toArm() = "\tADDS ${rd.toArm()}, ${rn.toArm()}, ${imm.toArm()}"
}
