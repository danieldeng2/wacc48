package generator.armInstructions

import generator.armInstructions.operands.Register

class PUSHInstr(private val reg: Register) : Instruction {
    override fun toString() = "\tPUSH {${reg.repr}}"
}