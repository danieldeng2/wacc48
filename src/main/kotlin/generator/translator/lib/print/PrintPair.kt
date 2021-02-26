package generator.translator.lib.print

import generator.instructions.Instruction
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
import generator.translator.TranslatorContext
import generator.translator.lib.LibaryFunction

object PrintPair : LibaryFunction {
    private var msgIndex: Int? = null
    override val label = "p_print_reference"

    override fun translate() = mutableListOf(
        LabelInstr(label),
        PUSHInstr(Register.LR),
        MOVInstr(Register.R1, Register.R0),
        LDRInstr(Register.R0, LabelOp(msgIndex!!)),
        ADDInstr(Register.R0, Register.R0, NumOp(4)),
        BLInstr("printf"),
        MOVInstr(Register.R0, NumOp(0)),
        BLInstr("fflush"),
        POPInstr(Register.PC)
    )

    override fun initIndex(ctx: TranslatorContext) {
        msgIndex = ctx.addMessage("%p\\0")
    }

}
