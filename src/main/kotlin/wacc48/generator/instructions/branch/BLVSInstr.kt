package wacc48.generator.instructions.branch

import wacc48.generator.instructions.Instruction

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
