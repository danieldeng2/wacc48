package generator.instructions

import generator.instructions.operands.Register

class Syscall(val syscallName: String) :
    Instruction {

    companion object {
        val requiredSyscalls: MutableSet<String> = mutableSetOf()
    }

    override fun tox86() = mutableListOf<String>().apply {
        requiredSyscalls.add(syscallName)

        add("\tpush ${Register.R0.tox86()}")
        add("\tcall $syscallName")
        add("\tadd ${Register.SP.tox86()}, 4")
    }

    override fun toArm() = "\tBL $syscallName"
}
