package generator.instructions

import generator.instructions.operands.LoadableOp
import generator.instructions.operands.Register

class CMPInstr(val rd: Register, val op: LoadableOp) : Instruction {
    override fun toString() = "\tCMP $rd, $op"
}
