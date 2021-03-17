package wacc48.generator.translator.lib.errors

import wacc48.generator.instructions.FunctionEnd
import wacc48.generator.instructions.FunctionStart
import wacc48.generator.instructions.branch.BLCSInstr
import wacc48.generator.instructions.branch.BLLTInstr
import wacc48.generator.instructions.compare.CMPInstr
import wacc48.generator.instructions.directives.LabelInstr
import wacc48.generator.instructions.load.LDRCSInstr
import wacc48.generator.instructions.load.LDRInstr
import wacc48.generator.instructions.load.LDRLTInstr
import wacc48.generator.instructions.operands.LabelOp
import wacc48.generator.instructions.operands.MemAddr
import wacc48.generator.instructions.operands.NumOp
import wacc48.generator.instructions.operands.Register
import wacc48.generator.translator.TranslatorContext
import wacc48.generator.translator.lib.LibraryFunction

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
