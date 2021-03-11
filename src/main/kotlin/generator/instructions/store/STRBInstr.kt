package generator.instructions.store

import generator.instructions.Instruction
import generator.instructions.operands.MemAddr
import generator.instructions.operands.Register

class STRBInstr(val reg: Register, val memAddr: MemAddr) : Instruction {

    override fun tox86() = listOf("\tSTRB ${reg.tox86()}, ${memAddr.tox86()}")

    override fun toArm() = "\tSTRB ${reg.toArm()}, ${memAddr.toArm()}"
}
