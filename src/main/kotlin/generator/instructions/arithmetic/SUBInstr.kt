package generator.instructions.arithmetic

import generator.instructions.Instruction
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register

class SUBInstr(val rd: Register, val rn: Register, val imm: NumOp) :
    Instruction {

    override fun tox86() =
        when {
            imm.value == 0 -> listOf("\tmov ${rd.tox86()}, ${rn.tox86()}")
            rd == rn -> listOf("\tsub ${rd.tox86()}, ${imm.tox86()}")
            else -> listOf(
                "\tmov ${rd.tox86()}, ${rn.tox86()}",
                "\tsub ${rd.tox86()}, ${imm.tox86()}"
            )
        }


    override fun toArm() = when (imm.value) {
        0 -> "\tSUB ${rd.toArm()}, ${rn.toArm()}"
        else -> "\tSUB ${rd.toArm()}, ${rn.toArm()}, ${imm.toArm()}"
    }
}
