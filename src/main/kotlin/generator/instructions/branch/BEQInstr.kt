package generator.instructions.branch

import generator.instructions.Instruction

class BEQInstr(val label: String) : Instruction {
    override fun toString() = "\tBEQ $label"
}
