package generator.instructions.arithmetic

import generator.instructions.Instruction
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register

class RSBSInstr(val rd: Register, val rn: Register, val imm: NumOp) :
    Instruction {

    override fun tox86() = listOf(
        "\tpush ${rn.tox86()}",
        "\tmov ${rd.tox86()}, ${imm.tox86()}",
        "\tsub ${rd.tox86()}, [${Register.SP.tox86()}]",
        "\tadd ${Register.SP.tox86()}, 4"
    )

    override fun toArm() = "\tRSBS ${rd.toArm()}, ${rn.toArm()}, ${imm.toArm()}"
}
