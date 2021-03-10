package generator.instructions.arithmetic

import generator.instructions.Instruction
import generator.instructions.operands.LoadableOp
import generator.instructions.operands.Register

class ADDInstr(val rd: Register, val rn: Register, val operand: LoadableOp?) :
    Instruction {

    override fun toArm() = when (operand) {
        null -> "\tADD ${rd.toArm()}, ${rn.toArm()}"
        else -> "\tADD ${rd.toArm()}, ${rn.toArm()}, ${operand.toArm()}"
    }
}