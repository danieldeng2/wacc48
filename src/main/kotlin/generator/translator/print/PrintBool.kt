package generator.translator.print

import generator.translator.TranslatorContext
import generator.armInstructions.*
import generator.armInstructions.operands.*

object PrintBool : PrintSyscall {
    override val label = "p_print_bool"
    private var trueIndex: Int? = null
    private var falseIndex: Int? = null

    override fun initIndex(ctx: TranslatorContext) {
        trueIndex = ctx.addMessage("true\\0")
        falseIndex = ctx.addMessage("false\\0")
    }

    override fun translate() =
        mutableListOf<Instruction>().apply {
            add(LabelInstr(label))
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