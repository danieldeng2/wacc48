package generator.armInstructions

class BInstr(val label: String) : Instruction {
    override fun toString() = "\tB $label"
}
