package generator.instructions.branch

import generator.instructions.Instruction

class BLVSInstr(val label: String) : Instruction {

    companion object {
        var counter: Int = 0
    }

    override fun tox86() = listOf(
        "\tjno __BLVS$counter",
        "\tcall $label",
        "__BLVS${counter++}:"
    )

    override fun toArm() = "\tBLVS $label"
}
