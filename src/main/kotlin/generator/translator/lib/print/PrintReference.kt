package generator.translator.lib.print

import generator.instructions.FunctionEnd
import generator.instructions.FunctionStart
import generator.instructions.Syscall
import generator.instructions.arithmetic.ADDInstr
import generator.instructions.directives.LabelInstr
import generator.instructions.load.LDRInstr
import generator.instructions.move.MOVInstr
import generator.instructions.operands.LabelOp
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.instructions.stack.PUSHInstr
import generator.translator.ArmConstants.NUM_BYTE_ADDRESS
import generator.translator.TranslatorContext
import generator.translator.lib.LibraryFunction

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
