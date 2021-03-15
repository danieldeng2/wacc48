package wacc48.generator.translator.lib.print

import wacc48.generator.instructions.FunctionEnd
import wacc48.generator.instructions.FunctionStart
import wacc48.generator.instructions.Syscall
import wacc48.generator.instructions.arithmetic.ADDInstr
import wacc48.generator.instructions.branch.BLInstr
import wacc48.generator.instructions.directives.LabelInstr
import wacc48.generator.instructions.load.LDRInstr
import wacc48.generator.instructions.move.MOVInstr
import wacc48.generator.instructions.operands.LabelOp
import wacc48.generator.instructions.operands.MemAddr
import wacc48.generator.instructions.operands.NumOp
import wacc48.generator.instructions.operands.Register
import wacc48.generator.instructions.stack.PUSHInstr
import wacc48.generator.translator.ArmConstants.NUM_BYTE_ADDRESS
import wacc48.generator.translator.TranslatorContext
import wacc48.generator.translator.lib.LibraryFunction

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
            Syscall("strlen"),
            PUSHInstr(Register.R0),
            LDRInstr(Register.R0, LabelOp(msgIndex!!)),
            Syscall("printf"),
            ADDInstr(Register.SP, Register.SP, NumOp(2 * NUM_BYTE_ADDRESS)),
            MOVInstr(Register.R0, NumOp(0)),
            Syscall("fflush"),
            FunctionEnd()
        )

}
