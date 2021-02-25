package generator.armInstructions.directives

import generator.armInstructions.Instruction

class Directive(private val directive: String) : Instruction {
    override fun toString() = "\t$directive"
}
