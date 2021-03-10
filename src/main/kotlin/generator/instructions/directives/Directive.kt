package generator.instructions.directives

import generator.instructions.Instruction

class Directive(private val directive: String) : Instruction {
    override fun toArm() = "\t$directive"
}
