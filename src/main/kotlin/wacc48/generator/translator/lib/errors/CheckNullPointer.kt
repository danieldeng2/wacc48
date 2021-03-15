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

object CheckNullPointer : LibraryFunction {
    private var msgIndex: Int? = null
    override val label = "p_check_null_pointer"

    override fun generateArm() = generateInstruction()

    override fun generatex86() = generateInstruction()

    private fun generateInstruction() = listOf(
        LabelInstr(label),
        FunctionStart(),
        CMPInstr(Register.R0, NumOp(0)),
        LDREQInstr(Register.R0, LabelOp(msgIndex!!)),
        BLEQInstr(RuntimeError.label),
        FunctionEnd()
    )


    override fun initIndex(ctx: TranslatorContext) {
        ctx.addLibraryFunction(RuntimeError)

        msgIndex = ctx.addMessage(
            "NullReferenceError: dereference a null reference\\n\\0"
        )
    }

}
