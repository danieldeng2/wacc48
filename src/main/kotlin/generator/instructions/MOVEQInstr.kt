package generator.instructions

import generator.instructions.operands.ImmOp
import generator.instructions.operands.Register

class MOVEQInstr(val rd: Register, val op: ImmOp) :
    Instruction {

    override fun toString() = "\tMOVEQ $rd, $op"
}
