package wacc48.generator.instructions.branch

import wacc48.generator.instructions.Instruction

class BLNEInstr(val label: String) : Instruction {

    companion object {
        var counter: Int = 0
    }

    override fun tox86() = listOf(
        "\tje __BLNE$counter",
        "\tcall $label",
        "__BLNE${counter++}:"
    )

    override fun toArm() = "\tBLNE $label"
}
