package generator.armInstructions

class LabelInstr(private val label: String) : Instruction {
    override fun toString() = "$label:"
}