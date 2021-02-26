package generator.instructions.branch

import generator.instructions.Instruction

class BLVSInstr(val label: String) : Instruction {

    override fun toString() = "\tBLVS $label"
}
