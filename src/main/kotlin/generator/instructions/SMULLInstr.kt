package generator.instructions

import generator.instructions.operands.Register

class SMULLInstr(
    val rdLow: Register,
    val rdHigh: Register,
    val rn: Register,
    val rm: Register
) : Instruction {

    override fun toString() = "\tSMULL $rdLow, $rdHigh, $rn, $rm"
}
