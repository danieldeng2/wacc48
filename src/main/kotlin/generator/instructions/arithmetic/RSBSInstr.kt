package generator.instructions.arithmetic

import generator.instructions.Instruction
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register

class RSBSInstr(val rd: Register, val rn: Register, val imm: NumOp) :
    Instruction {

    override fun toString() = "\tRSBS $rd, $rn, $imm"
}
