package wacc48.generator.instructions.arithmetic

import wacc48.generator.instructions.Instruction
import wacc48.generator.instructions.operands.Register

class SMULLInstr(
    val rdLow: Register,
    val rdHigh: Register,
    val rn: Register,
    val rm: Register
) : Instruction {

    override fun tox86() = listOf(
        "\tmov ${Register.R0.tox86()}, ${rn.tox86()}",
        "\timul ${rm.tox86()}",
        "\tpush edx",
        "\tpush eax",
        "\tpop ${rdLow.tox86()}",
        "\tpop ${rdHigh.tox86()}"
    )


    override fun toArm() =
        "\tSMULL ${rdLow.toArm()}, ${rdHigh.toArm()}, ${rn.toArm()}, ${rm.toArm()}"
}
