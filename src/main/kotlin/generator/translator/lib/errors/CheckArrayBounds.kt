package generator.translator.lib.errors

import generator.instructions.FunctionEnd
import generator.instructions.FunctionStart
import generator.instructions.Instruction
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
import generator.translator.TranslatorContext
import generator.translator.lib.LibraryFunction

object CheckArrayBounds : LibraryFunction {
    override val label = "p_check_array_bounds"
    private var negMsgIndex: Int? = null
    private var outOfBoundsMsgIndex: Int? = null


    override fun generateArm() = listOf(
        LabelInstr(label),
        FunctionStart(),
        CMPInstr(Register.R0, NumOp(0)),
        LDRLTInstr(Register.R0, LabelOp(negMsgIndex!!)),
        BLLTInstr(RuntimeError.label),
        LDRInstr(Register.R1, MemAddr(Register.R4)),
        CMPInstr(Register.R0, Register.R1),
        LDRCSInstr(Register.R0, LabelOp(outOfBoundsMsgIndex!!)),
        BLCSInstr(RuntimeError.label),
        FunctionEnd()
    )

    override fun generatex86() = listOf(
        LabelInstr(label),
        FunctionStart(),
        CMPInstr(Register.R0, NumOp(0)),
        LDRLTInstr(Register.R0, LabelOp(negMsgIndex!!)),
        BLLTInstr(RuntimeError.label),
        LDRInstr(Register.R1, MemAddr(Register.R4)),
        CMPInstr(Register.R0, Register.R1),
        LDRCSInstr(Register.R0, LabelOp(outOfBoundsMsgIndex!!)),
        BLCSInstr(RuntimeError.label),
        FunctionEnd()
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
