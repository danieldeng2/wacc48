package generator.instructions.store

import generator.instructions.Instruction
import generator.instructions.operands.MemAddr
import generator.instructions.operands.Register

class STRBInstr(val reg: Register, val memAddr: MemAddr) : Instruction {

    override fun toArm() = "\tSTRB ${reg.repr}, ${memAddr.toArm()}"
}
