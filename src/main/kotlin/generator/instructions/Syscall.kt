package generator.instructions

class Syscall(val syscallName: String) :
    Instruction {

    override fun tox86() = mutableListOf<String>().apply {
        add("\tmov ebx, eax")
        when (syscallName) {
            "exit" -> {
                add("\tmov eax, 1")
                add("\tsyscall")
            }
        }
    }

    override fun toArm() = "\tBL $syscallName"
}
