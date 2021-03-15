package generator.instructions.move

import generator.instructions.Instruction
import generator.instructions.operands.LoadableOp
import generator.instructions.operands.Register

class MOVInstr(val reg: Register, val value: LoadableOp) : Instruction {

    override fun tox86() = listOf("\tmov ${reg.tox86()}, ${value.tox86()}")

    override fun toArm() = "\tMOV ${reg.toArm()}, ${value.toArm()}"
}