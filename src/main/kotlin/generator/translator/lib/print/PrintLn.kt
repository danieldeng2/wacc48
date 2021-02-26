package generator.translator.lib.print

import generator.translator.TranslatorContext
import generator.instructions.*
import generator.instructions.arithmetic.ADDInstr
import generator.instructions.branch.BLInstr
import generator.instructions.directives.LabelInstr
import generator.instructions.load.LDRInstr
import generator.instructions.move.MOVInstr
import generator.instructions.operands.LabelOp
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.instructions.stack.POPInstr
import generator.instructions.stack.PUSHInstr
import generator.translator.lib.LibaryFunction

object PrintLn : LibaryFunction {
    override val label = "p_print_ln"
    private var msgIndex: Int? = null

    override fun initIndex(ctx: TranslatorContext) {
        msgIndex = ctx.addMessage("\\0")
    }

    override fun translate() =
        mutableListOf<Instruction>().apply {
            add(LabelInstr(label))
            add(PUSHInstr(Register.LR))
            add(LDRInstr(Register.R0, LabelOp(msgIndex!!)))
            add(ADDInstr(Register.R0, Register.R0, NumOp(4)))
            add(BLInstr("puts"))
            add(MOVInstr(Register.R0, NumOp(0)))
            add(BLInstr("fflush"))
            add(POPInstr(Register.PC))
        }
}
