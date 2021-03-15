package wacc48.generator.instructions.branch

import wacc48.generator.instructions.Instruction

class BLCSInstr(val label: String) : Instruction {

    companion object {
        var counter: Int = 0
    }

    override fun tox86() = listOf(
        "\tjb __BLCS$counter",
        "\tcall $label",
        "__BLCS${counter++}:"
    )

    override fun toArm() = "\tBLCS $label"
}
