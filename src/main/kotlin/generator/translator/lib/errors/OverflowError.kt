package generator.translator.lib.errors

import generator.instructions.BLInstr
import generator.instructions.Instruction
import generator.instructions.LDRInstr
import generator.instructions.LabelInstr
import generator.instructions.operands.LabelOp
import generator.instructions.operands.Register
import generator.translator.TranslatorContext
import generator.translator.lib.LibaryFunction

object OverflowError : LibaryFunction {

    override val label = "p_throw_overflow_error"
    private var msgIndex: Int? = null

    override fun translate() = mutableListOf<Instruction>().apply {
        add(LabelInstr(label))
        add(LDRInstr(Register.R0, LabelOp(msgIndex!!)))
        add(BLInstr(RuntimeError.label))
    }

    override fun initIndex(ctx: TranslatorContext) {
        msgIndex =
            ctx.addMessage(
                "OverflowError: the result is too small/large " +
                        "to store in a 4-byte signed-integer.\\n"
            )

        ctx.addPrintFunc(RuntimeError)
    }

}
