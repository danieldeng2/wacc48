package generator.instructions.logical

import generator.instructions.Instruction
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register

class EORInstr(val rd: Register, val rn: Register, val imm: NumOp?) :
    Instruction {
    override fun toArm() = when (imm) {
        null -> "\tEOR ${rd.toArm()}, ${rn.toArm()}"
        else -> "\tEOR ${rd.toArm()}, ${rn.toArm()}, ${imm.toArm()}"
    }
}
