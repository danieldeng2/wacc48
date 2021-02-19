package generator.armInstructions

import generator.armInstructions.operands.LoadableOp
import generator.armInstructions.operands.Register

class MOVInstr(val reg: Register, val value: LoadableOp) : Instruction {

    override fun toString() = "\tMOV $reg, $value"
}