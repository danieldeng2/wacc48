package generator.translator.helpers

import generator.instructions.operands.Register
import generator.instructions.stack.POPInstr
import generator.instructions.stack.PUSHInstr
import generator.translator.TranslatorContext


fun pushAndIncrement(
    ctx: TranslatorContext,
    vararg registers: Register
): PUSHInstr {
    ctx.stackPtrOffset += 4 * registers.size
    return PUSHInstr(*registers)
}

fun popAndDecrement(
    ctx: TranslatorContext,
    vararg registers: Register
): POPInstr {
    ctx.stackPtrOffset -= 4 * registers.size
    return POPInstr(*registers)
}