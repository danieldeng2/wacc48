package generator.armInstructions

import generator.armInstructions.operands.ImmOp
import generator.armInstructions.operands.Register

class ADDInstr(val rd: Register, val rn: Register, val imm: ImmOp?) :
    Instruction {

    override fun toString() = when (imm) {
        null -> "$\tADD $rd, $rn"
        else -> "$\tADD $rd, $rn, $imm"
    }
}