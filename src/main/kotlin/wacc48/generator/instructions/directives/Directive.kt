package wacc48.generator.instructions.directives

import wacc48.generator.instructions.Instruction

class Directive(private val directive: String) : Instruction {

    override fun tox86() = emptyList<String>()

    override fun toArm() = "\t$directive"
}
