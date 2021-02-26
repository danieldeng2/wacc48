package generator.instructions.logical

import generator.instructions.Instruction
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register

class EORInstr(val rd: Register, val rn: Register, val imm: NumOp?) :
    Instruction {
    override fun toString() = when (imm) {
        null -> "\tEOR $rd, $rn"
        else -> "\tEOR $rd, $rn, $imm"
    }
}
