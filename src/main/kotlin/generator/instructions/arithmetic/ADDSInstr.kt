package generator.instructions.arithmetic

import generator.instructions.Instruction
import generator.instructions.operands.LoadableOp
import generator.instructions.operands.Register

class ADDSInstr(val rd: Register, val rn: Register, val imm: LoadableOp) :
    Instruction {

    override fun tox86() = listOf(
        "\tmov ${rd.tox86()}, ${rn.tox86()}",
        "\tadd ${rd.tox86()}, ${imm.tox86()}"
    )

    override fun toArm() = "\tADDS ${rd.toArm()}, ${rn.toArm()}, ${imm.toArm()}"
}
