package wacc48.generator.instructions.branch

import wacc48.generator.instructions.Instruction

class BEQInstr(val label: String) : Instruction {

    override fun tox86() = listOf("\tje $label")

    override fun toArm() = "\tBEQ $label"
}
