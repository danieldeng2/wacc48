package wacc48.generator.instructions.arithmetic

import wacc48.generator.instructions.Instruction
import wacc48.generator.instructions.Syscall
import wacc48.generator.instructions.operands.Register

class IDIVInstr : Instruction {

    override fun tox86() = listOf(
        "\tmov edx, ${Register.R0.tox86()}",
        "\tsar edx, 31",
        "\tidiv ${Register.R1.tox86()}"
    )

    override fun toArm() = Syscall("__aeabi_idiv").toArm()

}
