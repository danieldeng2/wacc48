package generator.armInstructions

import generator.armInstructions.operands.ImmOp
import generator.armInstructions.operands.Register

class MOVNEInstr(val rd: Register, val op: ImmOp) :
    Instruction {

    override fun toString() = "\tMOVNE $rd, $op"
}
