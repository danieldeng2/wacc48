package generator.instructions.store

import generator.instructions.Instruction
import generator.instructions.operands.MemAddr
import generator.instructions.operands.Register

class STRBInstr(val reg: Register, val memAddr: MemAddr) : Instruction {

    override fun tox86() =
        listOf("\tmov byte ${memAddr.tox86()}, ${reg.x86AddressByte(1)}")

    override fun toArm() = "\tSTRB ${reg.toArm()}, ${memAddr.toArm()}"
}
