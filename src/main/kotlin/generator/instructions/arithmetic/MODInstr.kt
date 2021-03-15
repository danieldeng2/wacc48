package generator.instructions.arithmetic

import generator.instructions.Instruction
import generator.instructions.Syscall
import generator.instructions.operands.Register

class MODInstr : Instruction {

    override fun tox86() = listOf(
        "\tmov edx, ${Register.R0.tox86()}",
        "\tsar edx, 31",
        "\tidiv ${Register.R1.tox86()}",
        "\tmov ${Register.R1.tox86()}, edx"
    )

    override fun toArm() = Syscall("__aeabi_idivmod").toArm()

}
