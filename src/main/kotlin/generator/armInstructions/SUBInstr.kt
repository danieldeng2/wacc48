package generator.armInstructions

import generator.armInstructions.operands.NumOp
import generator.armInstructions.operands.Register

class SUBInstr(val rd: Register, val rn: Register, val imm: NumOp) :
    Instruction {

    override fun toString() = when (imm.value) {
        0 -> "\tSUB ${rd.repr}, ${rn.repr}"
        else -> "\tSUB ${rd.repr}, ${rn.repr}, $imm"
    }
}
