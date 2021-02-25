package generator.translator.print

import generator.translator.TranslatorContext
import generator.armInstructions.*
import generator.armInstructions.operands.*

object PrintBool : PrintSyscall {
    override var label = "p_print_bool"
    private var trueIndex: Int? = null
    private var falseIndex: Int? = null

    private val trueFormatter = "true\\0"
    private val falseFormatter = "false\\0"

    override fun initFormatters(ctx: TranslatorContext) {
        trueIndex = ctx.addMessage(trueFormatter)
        falseIndex = ctx.addMessage(falseFormatter)
    }

    override fun translate() =
        mutableListOf<Instruction>().apply {
            add(LabelInstr("p_print_bool"))
            add(PUSHInstr(Register.LR))
            add(CMPInstr(Register.R0, NumOp(0)))
            add(LDRNEInstr(Register.R0, LabelOp(trueIndex!!)))
            add(LDREQInstr(Register.R0, LabelOp(falseIndex!!)))
            add(ADDInstr(Register.R0, Register.R0, NumOp(4)))
            add(BLInstr("printf"))
            add(MOVInstr(Register.R0, NumOp(0)))
            add(BLInstr("fflush"))
            add(POPInstr(Register.PC))
        }
}