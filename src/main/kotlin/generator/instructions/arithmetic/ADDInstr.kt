package generator.instructions.arithmetic

import generator.instructions.Instruction
import generator.instructions.operands.ImmOp
import generator.instructions.operands.LoadableOp
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register

class ADDInstr(val rd: Register, val rn: Register, val operand: LoadableOp?) :
    Instruction {

    override fun toString() = when (operand) {
        null -> "\tADD ${rd.repr}, ${rn.repr}"
        else -> "\tADD ${rd.repr}, ${rn.repr}, $operand"
    }
}