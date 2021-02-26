package generator.instructions

class BInstr(val label: String) : Instruction {
    override fun toString() = "\tB $label"
}
