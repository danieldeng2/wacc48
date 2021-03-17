package wacc48.generator.instructions.move

import wacc48.generator.instructions.Instruction
import wacc48.generator.instructions.operands.LoadableOp
import wacc48.generator.instructions.operands.Register

class MOVInstr(val reg: Register, val value: LoadableOp) : Instruction {

    override fun tox86() = listOf("\tmov ${reg.tox86()}, ${value.tox86()}")

    override fun toArm() = "\tMOV ${reg.toArm()}, ${value.toArm()}"
}