package wacc48.generator.instructions.arithmetic

import wacc48.generator.instructions.Instruction
import wacc48.generator.instructions.operands.LoadableOp
import wacc48.generator.instructions.operands.Register

class ADDSInstr(val rd: Register, val rn: Register, val imm: LoadableOp) :
    Instruction {

    override fun tox86() = listOf(
        "\tmov ${rd.tox86()}, ${rn.tox86()}",
        "\tadd ${rd.tox86()}, ${imm.tox86()}"
    )

    override fun toArm() = "\tADDS ${rd.toArm()}, ${rn.toArm()}, ${imm.toArm()}"
}
