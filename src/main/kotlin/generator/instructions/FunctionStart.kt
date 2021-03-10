package generator.instructions

class FunctionStart : Instruction {

    override fun tox86() = listOf("\tpush ebp", "\tmov ebp, esp")

    override fun toArm(): String = "\tPUSH {lr}"

}
