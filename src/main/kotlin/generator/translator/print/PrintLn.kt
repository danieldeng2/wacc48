package generator.translator.print

import generator.translator.TranslatorContext
import generator.armInstructions.*
import generator.armInstructions.operands.LabelOp
import generator.armInstructions.operands.NumOp
import generator.armInstructions.operands.Register

object PrintLn : PrintSyscall {
    override var label = "p_print_ln"
    private var msgIndex: Int? = null
    private var formatter = "\\0"

    override fun initFormatters(ctx: TranslatorContext) {
        msgIndex = ctx.addMessage(formatter)
    }

    override fun translate() =
        mutableListOf<Instruction>().apply {
            add(LabelInstr("p_print_ln"))
            add(PUSHInstr(Register.LR))
            add(LDRInstr(Register.R0, LabelOp(msgIndex!!)))
            add(ADDInstr(Register.R0, Register.R0, NumOp(4)))
            add(BLInstr("puts"))
            add(MOVInstr(Register.R0, NumOp(0)))
            add(BLInstr("fflush"))
            add(POPInstr(Register.PC))
        }
}
