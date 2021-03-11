package generator.instructions

import generator.instructions.operands.Register

class Syscall(val syscallName: String) : Instruction {

    init {
        requiredSyscalls.add(syscallName)
    }

    companion object {
        val requiredSyscalls: MutableSet<String> = mutableSetOf()
    }

    override fun tox86() = listOf(
        "\tpush ${Register.R0.tox86()}",
        "\tcall $syscallName",
        "\tadd ${Register.SP.tox86()}, 4",
    )

    override fun toArm() = "\tBL $syscallName"
}
