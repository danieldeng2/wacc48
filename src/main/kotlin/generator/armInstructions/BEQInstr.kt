package generator.armInstructions

class BEQInstr(val label: String) : Instruction {
    override fun toString() = "\tBEQ $label"
}
