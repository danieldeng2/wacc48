package generator.instructions.load

import generator.instructions.Instruction
import generator.instructions.operands.LoadableOp
import generator.instructions.operands.Register

class LDRLTInstr(val rd: Register, val op: LoadableOp) : Instruction {
    override fun toString() = "\tLDRLT $rd, $op"
}
