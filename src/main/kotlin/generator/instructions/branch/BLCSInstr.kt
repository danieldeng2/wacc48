package generator.instructions.branch

import generator.instructions.Instruction

class BLCSInstr(val label: String) : Instruction {
    override fun toString() = "\tBLCS $label"
}
