package generator.instructions.branch

import generator.instructions.Instruction

class BInstr(val label: String) : Instruction {
    override fun toString() = "\tB $label"
}
