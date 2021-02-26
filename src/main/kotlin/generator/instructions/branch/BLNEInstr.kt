package generator.instructions.branch

import generator.instructions.Instruction

class BLNEInstr(val label: String) : Instruction {
    override fun toString() = "\tBLNE $label"
}
