package generator.instructions.logical

import generator.instructions.Instruction
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register

class EORInstr(val rd: Register, val rn: Register, val imm: NumOp?) :
    Instruction {

    override fun tox86() =
        when (imm) {
            null -> listOf("\tmov ${rd.tox86()}, ${rn.tox86()}")
            else -> listOf(
                "\tmov ${rd.tox86()}, ${rn.tox86()}",
                "\txor ${rd.tox86()}, ${imm.tox86()}"
            )
        }


    override fun toArm() = when (imm) {
        null -> "\tEOR ${rd.toArm()}, ${rn.toArm()}"
        else -> "\tEOR ${rd.toArm()}, ${rn.toArm()}, ${imm.toArm()}"
    }
}
