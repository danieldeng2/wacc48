package wacc48.generator.translator.lib.errors

import wacc48.generator.instructions.FunctionEnd
import wacc48.generator.instructions.FunctionStart
import wacc48.generator.instructions.branch.BLEQInstr
import wacc48.generator.instructions.compare.CMPInstr
import wacc48.generator.instructions.directives.LabelInstr
import wacc48.generator.instructions.load.LDREQInstr
import wacc48.generator.instructions.operands.LabelOp
import wacc48.generator.instructions.operands.NumOp
import wacc48.generator.instructions.operands.Register
import wacc48.generator.translator.TranslatorContext
import wacc48.generator.translator.lib.LibraryFunction

object DivideByZeroError : LibraryFunction {
    private var msgIndex: Int? = null
    override val label = "p_check_divide_by_zero"

    override fun generateArm() = generateInstruction()

    override fun generatex86() = generateInstruction()

    private fun generateInstruction() = listOf(
        LabelInstr(label),
        FunctionStart(),
        CMPInstr(Register.R1, NumOp(0)),
        LDREQInstr(Register.R0, LabelOp(msgIndex!!)),
        BLEQInstr(RuntimeError.label),
        FunctionEnd()
    )

    override fun initIndex(ctx: TranslatorContext) {
        msgIndex =
            ctx.addMessage(
                "DivideByZeroError: divide or modulo by zero\\n\\0"
            )
        ctx.addLibraryFunction(RuntimeError)
    }

}
