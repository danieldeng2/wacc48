package generator.armInstructions

import generator.armInstructions.operands.LoadableOp
import generator.armInstructions.operands.Register

class LDRSBInstr(
    private val reg: Register,
    private val op: LoadableOp
) : Instruction {

    override fun toString() = "\tLDRSB $reg, $op"
}