package generator.armInstructions

import generator.armInstructions.operands.LoadableOp
import generator.armInstructions.operands.Register

class LDREQInstr(
    private val reg: Register,
    private val op: LoadableOp
) : Instruction {
    override fun toString() = "\tLDREQ ${reg.repr}, $op"
}