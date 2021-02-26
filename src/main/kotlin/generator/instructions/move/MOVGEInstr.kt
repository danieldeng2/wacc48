package generator.instructions.move

import generator.instructions.Instruction
import generator.instructions.operands.ImmOp
import generator.instructions.operands.Register

class MOVGEInstr(val rd: Register, val imm: ImmOp) :
    Instruction {

    override fun toString() = "\tMOVGE $rd, $imm"
}
