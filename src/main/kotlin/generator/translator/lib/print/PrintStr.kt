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
import generator.instructions.operands.MemAddr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.instructions.stack.PUSHInstr
import generator.translator.ArmConstants.NUM_BYTE_ADDRESS
import generator.translator.TranslatorContext
import generator.translator.lib.LibraryFunction

object PrintStr : LibraryFunction {
    override val label = "p_print_string"
    private var msgIndex: Int? = null

    override fun initIndex(ctx: TranslatorContext) {
        msgIndex = ctx.addMessage("%.*s\\0")
    }

    override fun generateArm() =
        listOf(
            LabelInstr(label),
            FunctionStart(),
            LDRInstr(Register.R1, MemAddr(Register.R0)),
            ADDInstr(Register.R2, Register.R0, NumOp(NUM_BYTE_ADDRESS)),
            LDRInstr(Register.R0, LabelOp(msgIndex!!)),
            ADDInstr(Register.R0, Register.R0, NumOp(NUM_BYTE_ADDRESS)),
            BLInstr("printf"),
            MOVInstr(Register.R0, NumOp(0)),
            BLInstr("fflush"),
            FunctionEnd()
        )

    override fun generatex86() =
        listOf(
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

}
