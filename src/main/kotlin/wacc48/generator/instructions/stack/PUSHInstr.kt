package wacc48.generator.instructions.stack

import wacc48.generator.instructions.Instruction
import wacc48.generator.instructions.operands.Register

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