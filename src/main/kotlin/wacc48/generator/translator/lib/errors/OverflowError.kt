package wacc48.generator.translator.lib.errors

import wacc48.generator.instructions.branch.BLInstr
import wacc48.generator.instructions.directives.LabelInstr
import wacc48.generator.instructions.load.LDRInstr
import wacc48.generator.instructions.operands.LabelOp
import wacc48.generator.instructions.operands.Register
import wacc48.generator.translator.TranslatorContext
import wacc48.generator.translator.lib.LibraryFunction

object OverflowError : LibraryFunction {

    override val label = "p_throw_overflow_error"
    private var msgIndex: Int? = null

    override fun generateArm() = generateInstruction()

    override fun generatex86() = generateInstruction()

    private fun generateInstruction() = listOf(
        LabelInstr(label),
        LDRInstr(Register.R0, LabelOp(msgIndex!!)),
        BLInstr(RuntimeError.label)
    )

    override fun initIndex(ctx: TranslatorContext) {
        msgIndex =
            ctx.addMessage(
                "OverflowError: the result is too small/large " +
                        "to store in a 4-byte signed-integer.\\n"
            )

        ctx.addLibraryFunction(RuntimeError)
    }

}
