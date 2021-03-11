package generator.translator.lib.errors

import generator.instructions.Instruction
import generator.instructions.branch.BLInstr
import generator.instructions.directives.LabelInstr
import generator.instructions.load.LDRInstr
import generator.instructions.operands.LabelOp
import generator.instructions.operands.Register
import generator.translator.TranslatorContext
import generator.translator.lib.LibraryFunction

object OverflowError : LibraryFunction {

    override val label = "p_throw_overflow_error"
    private var msgIndex: Int? = null

    override fun generateArm() = mutableListOf<Instruction>().apply {
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

        ctx.addLibraryFunction(RuntimeError)
    }

}
