package generator.instructions.move

import generator.instructions.Instruction
import generator.instructions.operands.LoadableOp
import generator.instructions.operands.Register

class MOVGTInstr(val rd: Register, val imm: LoadableOp) :
    Instruction {
    override fun toString() = "\tMOVGT $rd, $imm"
}
