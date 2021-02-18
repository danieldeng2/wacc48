package generator.armInstructions

class Directive(private val directive: String) : Instruction {
    override fun toString() = directive
}
