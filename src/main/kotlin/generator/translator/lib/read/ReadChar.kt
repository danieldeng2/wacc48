package generator.translator.lib.read

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
import generator.translator.ArmConstants.NUM_BYTE_ADDRESS
import generator.translator.TranslatorContext
import generator.translator.lib.LibraryFunction

object ReadChar : LibraryFunction {
    override val label: String = "p_read_char"
    private var msgIndex: Int? = null

    override fun initIndex(ctx: TranslatorContext) {
        msgIndex = ctx.addMessage(" %c\\0")
    }

    override fun translate(): List<Instruction> =
        mutableListOf<Instruction>().apply {
            add(LabelInstr(label))
            add(PUSHInstr(Register.LR))
            add(MOVInstr(Register.R1, Register.R0))
            add(LDRInstr(Register.R0, LabelOp(msgIndex!!)))
            add(ADDInstr(Register.R0, Register.R0, NumOp(NUM_BYTE_ADDRESS)))
            add(BLInstr("scanf"))
            add(POPInstr(Register.PC))
        }

}