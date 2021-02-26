package generator.instructions

import generator.instructions.operands.LoadableOp
import generator.instructions.operands.Register

class LDRSBInstr(
    private val reg: Register,
    private val op: LoadableOp
) : Instruction {

    override fun toString() = "\tLDRSB $reg, $op"
}