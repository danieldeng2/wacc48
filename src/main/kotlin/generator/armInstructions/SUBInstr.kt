package generator.armInstructions

import generator.armInstructions.operands.NumOp
import generator.armInstructions.operands.Register

class SUBInstr(val rd: Register, val rn: Register, val imm: NumOp?) :
    Instruction {

    override fun toString() = when (imm) {
        null -> "$\tSUB $rd, $rn"
        else -> "$\tSUB $rd, $rn, $imm"
    }
}
