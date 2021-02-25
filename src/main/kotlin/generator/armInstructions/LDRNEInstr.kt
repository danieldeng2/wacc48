package generator.armInstructions

import generator.armInstructions.operands.LoadableOp
import generator.armInstructions.operands.Register

class LDRNEInstr(
    private val reg: Register,
    private val op: LoadableOp
) : Instruction {
    override fun toString() = "\tLDRNE ${reg.repr}, $op"
}