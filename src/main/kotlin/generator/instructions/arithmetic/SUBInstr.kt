package generator.instructions.arithmetic

import generator.instructions.Instruction
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register

class SUBInstr(val rd: Register, val rn: Register, val imm: NumOp) :
    Instruction {

    override fun toString() = when (imm.value) {
        0 -> "\tSUB ${rd.repr}, ${rn.repr}"
        else -> "\tSUB ${rd.repr}, ${rn.repr}, $imm"
    }
}
