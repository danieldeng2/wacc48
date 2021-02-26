package generator.instructions

class BEQInstr(val label: String) : Instruction {
    override fun toString() = "\tBEQ $label"
}
