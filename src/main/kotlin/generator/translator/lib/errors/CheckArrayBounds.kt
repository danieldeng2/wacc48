package generator.translator.lib.errors

import generator.instructions.branch.BLCSInstr
import generator.instructions.branch.BLLTInstr
import generator.instructions.compare.CMPInstr
import generator.instructions.directives.LabelInstr
import generator.instructions.load.LDRCSInstr
import generator.instructions.load.LDRInstr
import generator.instructions.load.LDRLTInstr
import generator.instructions.operands.LabelOp
import generator.instructions.operands.MemAddr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.instructions.stack.POPInstr
import generator.instructions.stack.PUSHInstr
import generator.translator.TranslatorContext
import generator.translator.lib.LibraryFunction

object CheckArrayBounds : LibraryFunction {
    override val label = "p_check_array_bounds"
    private var negMsgIndex: Int? = null
    private var outOfBoundsMsgIndex: Int? = null


    override fun translate() = listOf(
        LabelInstr(label),
        PUSHInstr(Register.LR),
        CMPInstr(Register.R0, NumOp(0)),
        LDRLTInstr(Register.R0, LabelOp(negMsgIndex!!)),
        BLLTInstr(RuntimeError.label),
        LDRInstr(Register.R1, MemAddr(Register.R4)),
        CMPInstr(Register.R0, Register.R1),
        LDRCSInstr(Register.R0, LabelOp(outOfBoundsMsgIndex!!)),
        BLCSInstr(RuntimeError.label),
        POPInstr(Register.PC)
    )

    override fun initIndex(ctx: TranslatorContext) {
        ctx.addLibraryFunction(RuntimeError)

        negMsgIndex = ctx.addMessage(
            "ArrayIndexOutOfBoundsError: negative index\\n\\0"
        )
        outOfBoundsMsgIndex = ctx.addMessage(
            "ArrayIndexOutOfBoundsError: index too large\\n\\0"
        )
    }

}
