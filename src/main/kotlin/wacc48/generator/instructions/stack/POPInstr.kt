package wacc48.generator.instructions.stack

import wacc48.generator.instructions.Instruction
import wacc48.generator.instructions.operands.Register

class POPInstr(private vararg val registers: Register) : Instruction {

    override fun tox86(): List<String> =
        registers.map { "\tpop ${it.tox86()}" }

    override fun toArm() = "\tPOP ${
        registers.joinToString(
            prefix = "{",
            postfix = "}",
            separator = ", "
        ) { it.toArm() }
    }"
}
