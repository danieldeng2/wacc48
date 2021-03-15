package generator.instructions.move

import generator.instructions.Instruction
import generator.instructions.operands.ImmOp
import generator.instructions.operands.Register

class MOVEQInstr(val rd: Register, val op: ImmOp) :
    Instruction {

    companion object {
        var counter = 0;
    }

    override fun tox86() = listOf(
        "\tjne _MOVEQ${counter}",
        "\tmov ${rd.tox86()}, ${op.tox86()}",
        "_MOVEQ${counter++}:"
    )
    override fun toArm() = "\tMOVEQ ${rd.toArm()}, ${op.toArm()}"
}
