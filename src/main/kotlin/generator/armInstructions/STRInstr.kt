package generator.armInstructions

import generator.armInstructions.operands.MemAddr
import generator.armInstructions.operands.Register

class STRInstr(val rd: Register, val memAddr: MemAddr) : Instruction {

    override fun toString() = "\tSTR ${rd.repr}, $memAddr"
}
