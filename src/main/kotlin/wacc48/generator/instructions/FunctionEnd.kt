package wacc48.generator.instructions

class FunctionEnd : Instruction {

    override fun tox86() = listOf("\tleave", "\tret")

    override fun toArm(): String = "\tPOP {pc}"

}
