package generator.instructions.stack

import generator.instructions.Instruction
import generator.instructions.operands.Register

class PUSHInstr(private vararg val registers: Register) : Instruction {

    override fun tox86(): List<String> =
        registers.reversed().map { "\tpush ${it.tox86()}" }


    override fun toArm() = "\tPUSH ${
        registers.joinToString(
            prefix = "{",
            postfix = "}",
            separator = ", "
        ) { it.toArm() }
    }"
}