package generator.instructions.move

import generator.instructions.Instruction
import generator.instructions.operands.LoadableOp
import generator.instructions.operands.Register

class MOVLEInstr(val rd: Register, val imm: LoadableOp) :
    Instruction {
    override fun toString() = "\tMOVLE $rd, $imm"
}
