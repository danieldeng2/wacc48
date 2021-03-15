package generator.instructions.arithmetic

import generator.instructions.Instruction
import generator.instructions.operands.LoadableOp
import generator.instructions.operands.Register
import generator.instructions.operands.ShiftOp

class ADDInstr(val rd: Register, val rn: Register, val op: LoadableOp?) :
    Instruction {

    override fun tox86() =
        when (op) {
            null -> listOf("\tadd ${rd.tox86()}, ${rn.tox86()}")
            is ShiftOp -> listOf(
                "\tpush ${op.reg.tox86()}",
                "\t${op.shiftType.x86repr} ${op.reg.tox86()}, ${op.num.tox86()}",
                "\tmov ${rd.tox86()}, ${rn.tox86()}",
                "\tadd ${rd.tox86()}, ${op.reg.tox86()}",
                "\tpop ${op.reg.tox86()}"
            )
            else -> listOf(
                "\tmov ${rd.tox86()}, ${rn.tox86()}",
                "\tadd ${rd.tox86()}, ${op.tox86()}"
            )
        }


    override fun toArm() = when (op) {
        null -> "\tADD ${rd.toArm()}, ${rn.toArm()}"
        else -> "\tADD ${rd.toArm()}, ${rn.toArm()}, ${op.toArm()}"
    }
}