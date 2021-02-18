package generator.armInstructions

import generator.armInstructions.operands.Register

class POPInstr(private val reg: Register) : Instruction {
    override fun toString() = "\tPOP {${reg.repr}}"
}
