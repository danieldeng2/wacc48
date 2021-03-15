package generator.translator.lib.print

import generator.instructions.FunctionEnd
import generator.instructions.FunctionStart
import generator.instructions.Syscall
import generator.instructions.arithmetic.ADDInstr
import generator.instructions.branch.BLInstr
import generator.instructions.directives.LabelInstr
import generator.instructions.load.LDRInstr
import generator.instructions.move.MOVInstr
import generator.instructions.operands.LabelOp
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.translator.ArmConstants.NUM_BYTE_ADDRESS
import generator.translator.TranslatorContext
import generator.translator.lib.LibraryFunction

object PrintLn : LibraryFunction {
    override val label = "p_print_ln"
    private var msgIndex: Int? = null

    override fun initIndex(ctx: TranslatorContext) {
        msgIndex = ctx.addMessage("\\0")
    }

    override fun generateArm() =
        listOf(
            LabelInstr(label),
            FunctionStart(),
            LDRInstr(Register.R0, LabelOp(msgIndex!!)),
            ADDInstr(Register.R0, Register.R0, NumOp(NUM_BYTE_ADDRESS)),
            BLInstr("puts"),
            MOVInstr(Register.R0, NumOp(0)),
            BLInstr("fflush"),
            FunctionEnd()
        )

    override fun generatex86() =
        listOf(
            LabelInstr(label),
            FunctionStart(),
            LDRInstr(Register.R0, LabelOp(msgIndex!!)),
            Syscall("puts"),
            MOVInstr(Register.R0, NumOp(0)),
            Syscall("fflush"),
            FunctionEnd()
        )

}
