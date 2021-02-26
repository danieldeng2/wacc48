package generator.instructions

import generator.instructions.operands.MemAddr
import generator.instructions.operands.Register

class STRInstr(val rd: Register, val memAddr: MemAddr) : Instruction {

    override fun toString() = "\tSTR ${rd.repr}, $memAddr"
}
