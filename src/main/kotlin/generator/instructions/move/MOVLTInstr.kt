package generator.instructions.move

import generator.instructions.Instruction
import generator.instructions.operands.ImmOp
import generator.instructions.operands.Register

class MOVLTInstr(val rd: Register, val imm: ImmOp) :
    Instruction {

    companion object {
        var counter = 0;
    }

    override fun tox86() = listOf(
        "\tjge _MOVLT${counter}",
        "\tmov ${rd.tox86()}, ${imm.tox86()}",
        "_MOVLT${counter++}:"
    )

    override fun toArm() = "\tMOVLT ${rd.toArm()}, ${imm.toArm()}"
}
