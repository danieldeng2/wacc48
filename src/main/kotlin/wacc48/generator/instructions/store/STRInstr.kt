package wacc48.generator.instructions.store

import wacc48.generator.instructions.Instruction
import wacc48.generator.instructions.operands.MemoryReference
import wacc48.generator.instructions.operands.Register

class STRInstr(val rd: Register, val memAddr: MemoryReference) : Instruction {

    override fun tox86() = mutableListOf<String>().apply {
        add("\tmov ${memAddr.tox86()}, ${rd.tox86()}")

        if (memAddr.updateBaseReg)
            add("\tsub ${Register.SP.tox86()}, 4")
    }

    override fun toArm() = "\tSTR ${rd.toArm()}, ${memAddr.toArm()}"
}

