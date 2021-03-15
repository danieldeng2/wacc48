package wacc48.generator.instructions.move

import wacc48.generator.instructions.Instruction
import wacc48.generator.instructions.operands.LoadableOp
import wacc48.generator.instructions.operands.Register

class MOVGTInstr(val rd: Register, val imm: LoadableOp) :
    Instruction {

    companion object {
        var counter = 0
    }

    override fun tox86() = listOf(
        "\tjle _MOVGT${counter}",
        "\tmov ${rd.tox86()}, ${imm.tox86()}",
        "_MOVGT${counter++}:"
    )

    override fun toArm() = "\tMOVGT ${rd.toArm()}, ${imm.toArm()}"
}
