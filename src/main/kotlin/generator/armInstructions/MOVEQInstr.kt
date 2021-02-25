package generator.armInstructions

import generator.armInstructions.operands.ImmOp
import generator.armInstructions.operands.Register

class MOVEQInstr(val rd: Register, val op: ImmOp) :
    Instruction {

    override fun toString() = "\tMOVEQ $rd, $op"
}
