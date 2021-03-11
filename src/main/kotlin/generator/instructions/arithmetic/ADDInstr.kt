package generator.instructions.arithmetic

import generator.instructions.Instruction
import generator.instructions.operands.LoadableOp
import generator.instructions.operands.Register

class ADDInstr(val rd: Register, val rn: Register, val operand: LoadableOp?) :
    Instruction {

    override fun tox86() =
        when {
            operand == null -> listOf("\tadd ${rd.tox86()}, ${rn.tox86()}")
            rd == rn -> listOf("\tadd ${rd.tox86()}, ${operand.tox86()}")
            else -> listOf(
                "\tmov ${rd.tox86()}, ${rn.tox86()}",
                "\tadd ${rd.tox86()} ${operand.tox86()}"
            )
        }


    override fun toArm() = when (operand) {
        null -> "\tADD ${rd.toArm()}, ${rn.toArm()}"
        else -> "\tADD ${rd.toArm()}, ${rn.toArm()}, ${operand.toArm()}"
    }
}