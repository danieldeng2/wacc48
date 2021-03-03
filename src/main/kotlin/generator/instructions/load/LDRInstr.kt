package generator.instructions.load

import generator.instructions.Instruction
import generator.instructions.operands.LoadableOp
import generator.instructions.operands.Register

class LDRInstr(
    private val reg: Register,
    private val op: LoadableOp,
) : Instruction {
    override fun toString() = "\tLDR ${reg.repr}, $op"
}