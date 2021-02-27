package generator.instructions.stack

import generator.instructions.Instruction
import generator.instructions.operands.Register

class PUSHInstr(private vararg val registers: Register) : Instruction {
    override fun toString() = "\tPUSH ${
        registers.joinToString(
            prefix = "{",
            postfix = "}",
            separator = ", "
        )
    }"
}