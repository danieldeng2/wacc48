package generator.instructions.stack

import generator.instructions.Instruction
import generator.instructions.operands.Register

class PUSHInstr(private val reg: Register) : Instruction {
    override fun toString() = "\tPUSH {${reg.repr}}"
}