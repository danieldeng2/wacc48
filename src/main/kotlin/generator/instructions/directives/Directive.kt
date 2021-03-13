package generator.instructions.directives

import generator.instructions.Instruction

class Directive(private val directive: String) : Instruction {

    override fun tox86() = emptyList<String>()

    override fun toArm() = "\t$directive"
}
