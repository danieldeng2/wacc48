package generator.translator.print

import generator.translator.TranslatorContext
import generator.armInstructions.*
import generator.armInstructions.operands.LabelOp
import generator.armInstructions.operands.NumOp
import generator.armInstructions.operands.Register

object PrintInt : PrintSyscall {
    override var label: String = "p_print_int"
    private var formatter: String = "%d\\0"
    private var msgIndex: Int? = null

    override fun initFormatters(ctx: TranslatorContext) {
        msgIndex = ctx.addMessage(formatter)
    }

    override fun translate() =
        mutableListOf<Instruction>().apply {
            add(LabelInstr("p_print_int"))
            add(PUSHInstr(Register.LR))
            add(MOVInstr(Register.R1, Register.R0))
            add(LDRInstr(Register.R0, LabelOp(msgIndex!!)))
            add(ADDInstr(Register.R0, Register.R0, NumOp(4)))
            add(BLInstr("printf"))
            add(MOVInstr(Register.R0, NumOp(0)))
            add(BLInstr("fflush"))
            add(POPInstr(Register.PC))
        }

}