package generator.instructions

import generator.instructions.operands.Register

class POPInstr(private val reg: Register) : Instruction {
    override fun toString() = "\tPOP {${reg.repr}}"
}
