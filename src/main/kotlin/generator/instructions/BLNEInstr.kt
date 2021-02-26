package generator.instructions

class BLNEInstr(val label: String) : Instruction {
    override fun toString() = "\tBLNE $label"
}
