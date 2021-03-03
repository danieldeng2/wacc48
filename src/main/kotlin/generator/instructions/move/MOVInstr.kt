package generator.instructions.move

import generator.instructions.Instruction
import generator.instructions.operands.LoadableOp
import generator.instructions.operands.Register

class MOVInstr(val reg: Register, val value: LoadableOp) : Instruction {

    override fun toString() = "\tMOV ${reg.repr}, $value"
}