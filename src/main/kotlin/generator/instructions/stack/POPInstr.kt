package generator.instructions.stack

import generator.instructions.Instruction
import generator.instructions.operands.Register

class POPInstr(private vararg val registers: Register) : Instruction {
    override fun toString() = "\tPOP ${
        registers.joinToString(
            prefix = "{",
            postfix = "}",
            separator = ", "
        )
    }"
}
