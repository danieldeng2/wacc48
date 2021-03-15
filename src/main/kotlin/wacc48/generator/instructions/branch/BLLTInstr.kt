package wacc48.generator.instructions.branch

import wacc48.generator.instructions.Instruction

class BLLTInstr(val label: String) : Instruction {

    companion object {
        var counter: Int = 0
    }

    override fun tox86() = listOf(
        "\tjge __BLLT$counter",
        "\tcall $label",
        "__BLLT${counter++}:"
    )

    override fun toArm() = "\tBLLT $label"
}
