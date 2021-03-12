package generator.instructions.branch

import generator.instructions.Instruction

class BLCSInstr(val label: String) : Instruction {

    companion object {
        var counter: Int = 0
    }

    override fun tox86() = listOf(
        "\tjae __BLCS$counter",
        "\tcall $label",
        "__BLCS${counter++}:"
    )

    override fun toArm() = "\tBLCS $label"
}
