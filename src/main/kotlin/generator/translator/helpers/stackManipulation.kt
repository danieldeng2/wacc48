package generator.translator.helpers

import generator.instructions.operands.Register
import generator.instructions.stack.POPInstr
import generator.instructions.stack.PUSHInstr
import generator.translator.ArmConstants.NUM_BYTE_ADDRESS
import generator.translator.TranslatorContext

/** Saves [registers] to the stack, incrementing stack pointer offset accordingly. */
fun pushAndIncrement(
    ctx: TranslatorContext,
    vararg registers: Register
): PUSHInstr {
    ctx.stackPtrOffset += NUM_BYTE_ADDRESS * registers.size
    return PUSHInstr(*registers)
}

/** Pops [registers] off the stack, reducing stack pointer offset accordingly. */
fun popAndDecrement(
    ctx: TranslatorContext,
    vararg registers: Register
): POPInstr {
    ctx.stackPtrOffset -= NUM_BYTE_ADDRESS * registers.size
    return POPInstr(*registers)
}
