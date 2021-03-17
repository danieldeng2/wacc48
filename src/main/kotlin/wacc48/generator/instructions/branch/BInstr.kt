package wacc48.generator.instructions.branch

import wacc48.generator.instructions.Instruction

class BInstr(val label: String) : Instruction {

    override fun tox86() = listOf("\tjmp $label")

    override fun toArm() = "\tB $label"
}
