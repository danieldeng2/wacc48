package generator.armInstructions

import generator.armInstructions.operands.MemAddr
import generator.armInstructions.operands.Register

class STRBInstr(val reg: Register, val memAddr: MemAddr) : Instruction {

    override fun toString() = "\tSTRB ${reg.repr}, $memAddr"
}
