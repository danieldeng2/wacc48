package wacc48.generator.translator.helpers

import wacc48.generator.instructions.operands.Register
import wacc48.generator.instructions.stack.POPInstr
import wacc48.generator.instructions.stack.PUSHInstr
import wacc48.generator.translator.ArmConstants.NUM_BYTE_ADDRESS
import wacc48.generator.translator.TranslatorContext

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
