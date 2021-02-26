package generator.instructions.arithmetic

import generator.instructions.Instruction
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register

class ADDInstr(val rd: Register, val rn: Register, val imm: NumOp?) :
    Instruction {

    override fun toString() = when (imm) {
        null -> "\tADD ${rd.repr}, ${rn.repr}"
        else -> "\tADD ${rd.repr}, ${rn.repr}, $imm"
    }
}