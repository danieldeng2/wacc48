package wacc48.generator.instructions.branch

import wacc48.generator.instructions.Instruction

class BLEQInstr(val label: String) : Instruction {

    companion object {
        var counter: Int = 0
    }

    override fun tox86() = listOf(
        "\tjne __BLEQ$counter",
        "\tcall $label",
        "__BLEQ${counter++}:"
    )

    override fun toArm() = "\tBLEQ $label"
}
