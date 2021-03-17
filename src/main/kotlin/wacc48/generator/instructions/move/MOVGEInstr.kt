package wacc48.generator.instructions.move

import wacc48.generator.instructions.Instruction
import wacc48.generator.instructions.operands.ImmOp
import wacc48.generator.instructions.operands.Register

class MOVGEInstr(val rd: Register, val imm: ImmOp) :
    Instruction {

    companion object {
        var counter = 0
    }

    override fun tox86() = listOf(
        "\tjl _MOVGE${counter}",
        "\tmov ${rd.tox86()}, ${imm.tox86()}",
        "_MOVGE${counter++}:"
    )
    override fun toArm() = "\tMOVGE ${rd.toArm()}, ${imm.toArm()}"
}
