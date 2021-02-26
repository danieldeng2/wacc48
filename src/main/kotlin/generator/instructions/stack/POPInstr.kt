package generator.instructions.stack

import generator.instructions.Instruction
import generator.instructions.operands.Register

class POPInstr(private val reg: Register) : Instruction {
    override fun toString() = "\tPOP {${reg.repr}}"
}
