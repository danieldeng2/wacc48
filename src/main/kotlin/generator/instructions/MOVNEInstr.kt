package generator.instructions

import generator.instructions.operands.ImmOp
import generator.instructions.operands.Register

class MOVNEInstr(val rd: Register, val op: ImmOp) :
    Instruction {

    override fun toString() = "\tMOVNE $rd, $op"
}
