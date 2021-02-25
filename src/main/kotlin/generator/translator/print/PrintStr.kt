package generator.translator.print

import generator.translator.TranslatorContext
import generator.armInstructions.*
import generator.armInstructions.operands.LabelOp
import generator.armInstructions.operands.MemAddr
import generator.armInstructions.operands.NumOp
import generator.armInstructions.operands.Register

object PrintStr : PrintSyscall {
    override var label = "p_print_string"
    private var formatter = "%.*s\\0"
    private var msgIndex: Int? = null

    override fun initFormatters(ctx: TranslatorContext) {
        msgIndex = ctx.addMessage(formatter)
    }

    override fun translate() =
        mutableListOf<Instruction>().apply {
            add(LabelInstr("p_print_string"))
            add(PUSHInstr(Register.LR))
            add(LDRInstr(Register.R1, MemAddr(Register.R0, NumOp(0))))
            add(ADDInstr(Register.R2, Register.R0, NumOp(4)))
            add(LDRInstr(Register.R0, LabelOp(msgIndex!!)))
            add(ADDInstr(Register.R0, Register.R0, NumOp(4)))
            add(BLInstr("printf"))
            add(MOVInstr(Register.R0, NumOp(0)))
            add(BLInstr("fflush"))
            add(POPInstr(Register.PC))
        }
}
