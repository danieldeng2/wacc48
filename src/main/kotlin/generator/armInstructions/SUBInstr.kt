package generator.armInstructions

import generator.armInstructions.operands.ImmOp
import generator.armInstructions.operands.Register

class SUBInstr(val rd: Register, val rn: Register, val imm: ImmOp?) :
    Instruction {

    override fun toString() = when (imm) {
        null -> "$\tSUB $rd, $rn"
        else -> "$\tSUB $rd, $rn, $imm"
    }
}
