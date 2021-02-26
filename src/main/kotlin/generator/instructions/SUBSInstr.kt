package generator.instructions

import generator.instructions.operands.LoadableOp
import generator.instructions.operands.Register

class SUBSInstr(val rd: Register, val rn: Register, val imm: LoadableOp) :
    Instruction {
    override fun toString() = "\tSUBS $rd, $rn, $imm"
}
