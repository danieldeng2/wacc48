package wacc48.generator.instructions.move

import wacc48.generator.instructions.Instruction
import wacc48.generator.instructions.operands.ImmOp
import wacc48.generator.instructions.operands.Register

class MOVNEInstr(val rd: Register, val op: ImmOp) :
    Instruction {

    companion object {
        var counter = 0
    }

    override fun tox86() = listOf(
        "\tje _MOVNE${counter}",
        "\tmov ${rd.tox86()}, ${op.tox86()}",
        "_MOVNE${counter++}:"
    )

    override fun toArm() = "\tMOVNE ${rd.toArm()}, ${op.toArm()}"
}
