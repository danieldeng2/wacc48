package generator.instructions.stack

import generator.instructions.Instruction
import generator.instructions.operands.Register

class POPInstr(private vararg val registers: Register) : Instruction {

    override fun tox86(): List<String> =
        registers.reversed().map { "\tpop ${it.tox86()}" }

    override fun toArm() = "\tPOP ${
        registers.joinToString(
            prefix = "{",
            postfix = "}",
            separator = ", "
        ) { it.toArm() }
    }"
}
