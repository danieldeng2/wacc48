package generator.instructions

import generator.instructions.operands.Register

class PUSHInstr(private val reg: Register) : Instruction {
    override fun toString() = "\tPUSH {${reg.repr}}"
}