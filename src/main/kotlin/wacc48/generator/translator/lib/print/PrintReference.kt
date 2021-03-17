package wacc48.generator.translator.lib.print

import wacc48.generator.instructions.FunctionEnd
import wacc48.generator.instructions.FunctionStart
import wacc48.generator.instructions.Syscall
import wacc48.generator.instructions.arithmetic.ADDInstr
import wacc48.generator.instructions.directives.LabelInstr
import wacc48.generator.instructions.load.LDRInstr
import wacc48.generator.instructions.move.MOVInstr
import wacc48.generator.instructions.operands.LabelOp
import wacc48.generator.instructions.operands.NumOp
import wacc48.generator.instructions.operands.Register
import wacc48.generator.instructions.stack.PUSHInstr
import wacc48.generator.translator.ArmConstants.NUM_BYTE_ADDRESS
import wacc48.generator.translator.TranslatorContext
import wacc48.generator.translator.lib.LibraryFunction

object PrintReference : LibraryFunction {
    private var msgIndex: Int? = null
    override val label = "p_print_reference"

    override fun generateArm() = listOf(
        LabelInstr(label),
        FunctionStart(),
        MOVInstr(Register.R1, Register.R0),
        LDRInstr(Register.R0, LabelOp(msgIndex!!)),
        ADDInstr(Register.R0, Register.R0, NumOp(NUM_BYTE_ADDRESS)),
        Syscall("printf"),
        MOVInstr(Register.R0, NumOp(0)),
        Syscall("fflush"),
        FunctionEnd()
    )

    override fun generatex86() = listOf(
        LabelInstr(label),
        FunctionStart(),
        PUSHInstr(Register.R0),
        LDRInstr(Register.R0, LabelOp(msgIndex!!)),
        Syscall("printf"),
        ADDInstr(Register.SP, Register.SP, NumOp(NUM_BYTE_ADDRESS)),
        MOVInstr(Register.R0, NumOp(0)),
        Syscall("fflush"),
        FunctionEnd()
    )

    override fun initIndex(ctx: TranslatorContext) {
        msgIndex = ctx.addMessage("%p\\0")
    }

}
