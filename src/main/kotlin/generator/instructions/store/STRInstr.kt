package generator.instructions.store

import generator.instructions.Instruction
import generator.instructions.operands.MemAddr
import generator.instructions.operands.Register

class STRInstr(val rd: Register, val memAddr: MemAddr) : Instruction {

    override fun tox86() = listOf("\tmov ${memAddr.tox86()}, ${rd.tox86()}")

    override fun toArm() = "\tSTR ${rd.toArm()}, ${memAddr.toArm()}"
}
