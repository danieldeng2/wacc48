package generator.instructions.branch

import generator.instructions.Instruction

class BEQInstr(val label: String) : Instruction {

    override fun tox86() = listOf("\tje $label")

    override fun toArm() = "\tBEQ $label"
}
