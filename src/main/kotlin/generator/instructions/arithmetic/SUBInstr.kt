package generator.instructions.arithmetic

import generator.instructions.Instruction
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register

class SUBInstr(val rd: Register, val rn: Register, val imm: NumOp) :
    Instruction {

    override fun toArm() = when (imm.value) {
        0 -> "\tSUB ${rd.toArm()}, ${rn.toArm()}"
        else -> "\tSUB ${rd.toArm()}, ${rn.toArm()}, ${imm.toArm()}"
    }
}
