package generator.armInstructions

import generator.armInstructions.operands.NumOp
import generator.armInstructions.operands.Register

class ADDInstr(val rd: Register, val rn: Register, val imm: NumOp?) :
    Instruction {

    override fun toString() = when (imm) {
        null -> "$\tADD $rd, $rn"
        else -> "$\tADD $rd, $rn, $imm"
    }
}