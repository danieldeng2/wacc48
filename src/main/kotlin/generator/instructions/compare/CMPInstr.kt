package generator.instructions.compare

import generator.instructions.Instruction
import generator.instructions.operands.LoadableOp
import generator.instructions.operands.Register
import generator.instructions.operands.ShiftOp

class CMPInstr(val rd: Register, val op: LoadableOp) : Instruction {

    override fun tox86() = when (op) {
        is ShiftOp -> listOf(
            "\tpush ${op.reg.tox86()}",
            "\t${op.shiftType.x86repr} ${op.reg.tox86()}, ${op.num.tox86()}",
            "\tcmp ${rd.tox86()}, ${op.reg.tox86()}",
            "\tpop ${op.reg.tox86()}"
        )
        else -> listOf("\tcmp ${rd.tox86()}, ${op.tox86()}")
    }

    override fun toArm() = "\tCMP ${rd.toArm()}, ${op.toArm()}"
}
