package wacc48.generator.instructions.arithmetic

import wacc48.generator.instructions.Instruction
import wacc48.generator.instructions.operands.NumOp
import wacc48.generator.instructions.operands.Register

class RSBSInstr(val rd: Register, val rn: Register, val imm: NumOp) :
    Instruction {

    override fun tox86() = listOf(
        "\tmov ${rd.tox86()}, ${rn.tox86()}",
        "\tsub ${rd.tox86()}, ${imm.tox86()}",
        "\tneg ${rd.tox86()}"
    )

    override fun toArm() = "\tRSBS ${rd.toArm()}, ${rn.toArm()}, ${imm.toArm()}"
}
