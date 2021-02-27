package generator.instructions.branch

import generator.instructions.Instruction

class BLLTInstr(val label: String) : Instruction {
    override fun toString() = "\tBLLT $label"
}
