package generator.instructions.branch

import generator.instructions.Instruction

class BLEQInstr(val label: String) : Instruction {
    override fun toArm() = "\tBLEQ $label"
}
