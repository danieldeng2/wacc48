package wacc48.generator.instructions.store

import wacc48.generator.instructions.Instruction
import wacc48.generator.instructions.operands.MemoryReference
import wacc48.generator.instructions.operands.Register

class STRBInstr(val reg: Register, val memAddr: MemoryReference) : Instruction {

    override fun tox86() = mutableListOf<String>().apply {
        add("\tmov byte ${memAddr.tox86()}, ${reg.x86AddressByte(1)}")

        if (memAddr.updateBaseReg)
            add("\tsub ${Register.SP.tox86()}, 1")
    }

    override fun toArm() = "\tSTRB ${reg.toArm()}, ${memAddr.toArm()}"
}
