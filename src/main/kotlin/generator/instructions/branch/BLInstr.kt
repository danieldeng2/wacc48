package generator.instructions.branch

import generator.instructions.Instruction

class BLInstr(val name: String) : Instruction {

    override fun tox86() = listOf("\tcall $name")

    override fun toArm() = "\tBL $name"
}
