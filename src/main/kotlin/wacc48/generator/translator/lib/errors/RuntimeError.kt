package wacc48.generator.translator.lib.errors

import wacc48.generator.instructions.Syscall
import wacc48.generator.instructions.branch.BLInstr
import wacc48.generator.instructions.directives.LabelInstr
import wacc48.generator.instructions.move.MOVInstr
import wacc48.generator.instructions.operands.NumOp
import wacc48.generator.instructions.operands.Register
import wacc48.generator.translator.TranslatorContext
import wacc48.generator.translator.lib.LibraryFunction
import wacc48.generator.translator.lib.print.PrintStr

object RuntimeError : LibraryFunction {
    override val label = "p_throw_runtime_error"

    override fun generateArm() = generateCode()

    override fun generatex86() = generateCode()

    private fun generateCode() = listOf(
        LabelInstr(label),
        BLInstr(PrintStr.label),
        MOVInstr(Register.R0, NumOp(-1)),
        Syscall("exit")
    )

    override fun initIndex(ctx: TranslatorContext) {
        ctx.addLibraryFunction(PrintStr)
    }

}
