package generator.instructions.move

import generator.instructions.Instruction
import generator.instructions.operands.LoadableOp
import generator.instructions.operands.Register

class MOVLEInstr(val rd: Register, val imm: LoadableOp) :
    Instruction {

    companion object {
        var counter = 0;
    }

    override fun tox86() = listOf(
        "\tjg _MOVLE${counter}",
        "\tmov ${rd.tox86()}, ${imm.tox86()}",
        "_MOVLE${counter++}:"
    )
    override fun toArm() = "\tMOVLE ${rd.toArm()}, ${imm.toArm()}"
}
