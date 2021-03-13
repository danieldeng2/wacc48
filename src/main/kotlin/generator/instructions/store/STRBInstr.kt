package generator.instructions.store

import generator.instructions.Instruction
import generator.instructions.operands.MemAddr
import generator.instructions.operands.MemoryReference
import generator.instructions.operands.Register

class STRBInstr(val reg: Register, val memAddr: MemoryReference) : Instruction {

    override fun tox86() = mutableListOf<String>().apply {
        add("\tmov byte ${memAddr.tox86()}, ${reg.x86AddressByte(1)}")

        if (memAddr.updateBaseReg)
            add("\tsub ${Register.SP.tox86()}, 1")
    }

    override fun toArm() = "\tSTRB ${reg.toArm()}, ${memAddr.toArm()}"
}
