package generator.armInstructions

import generator.armInstructions.Instruction
import generator.armInstructions.operands.LoadableOp
import generator.armInstructions.operands.NumOp
import generator.armInstructions.operands.Register

class CMPInstr(val rd: Register, val op: LoadableOp) : Instruction {
    override fun toString() = "\tCMP $rd, $op"
}
