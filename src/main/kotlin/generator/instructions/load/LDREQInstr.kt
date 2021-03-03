package generator.instructions.load

import generator.instructions.Instruction
import generator.instructions.operands.LoadableOp
import generator.instructions.operands.Register

class LDREQInstr(
    private val reg: Register,
    private val op: LoadableOp
) : Instruction {
    override fun toString() = "\tLDREQ ${reg.repr}, $op"
}