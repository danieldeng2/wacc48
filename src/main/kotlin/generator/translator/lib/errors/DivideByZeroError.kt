package generator.translator.lib.errors

import generator.instructions.FunctionEnd
import generator.instructions.FunctionStart
import generator.instructions.Instruction
import generator.instructions.branch.BLEQInstr
import generator.instructions.compare.CMPInstr
import generator.instructions.directives.LabelInstr
import generator.instructions.load.LDREQInstr
import generator.instructions.operands.LabelOp
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.translator.TranslatorContext
import generator.translator.lib.LibraryFunction

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
