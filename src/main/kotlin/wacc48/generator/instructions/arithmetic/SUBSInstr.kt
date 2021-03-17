package wacc48.generator.instructions.arithmetic

import wacc48.generator.instructions.Instruction
import wacc48.generator.instructions.operands.LoadableOp
import wacc48.generator.instructions.operands.Register

class SUBSInstr(val rd: Register, val rn: Register, val imm: LoadableOp) :
    Instruction {

    override fun tox86() = when {
        rd == rn -> listOf("\tsub ${rd.tox86()}, ${imm.tox86()}")
        else -> listOf(
            "\tmov ${rd.tox86()}, ${rn.tox86()}",
            "\tsub ${rd.tox86()}, ${imm.tox86()}"
        )
    }

    override fun toArm() = "\tSUBS ${rd.toArm()}, ${rn.toArm()}, ${imm.toArm()}"
}
