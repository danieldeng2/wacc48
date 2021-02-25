package generator.armInstructions

import generator.armInstructions.Instruction
import generator.armInstructions.operands.NumOp
import generator.armInstructions.operands.Register

class CMPInstr(val rd: Register, val imm: NumOp) : Instruction {
    override fun toString() = "\tCMP $rd, $imm"
}
