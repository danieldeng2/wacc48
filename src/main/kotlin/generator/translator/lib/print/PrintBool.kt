package generator.translator.lib.print

import generator.instructions.FunctionEnd
import generator.instructions.FunctionStart
import generator.instructions.Instruction
import generator.instructions.Syscall
import generator.instructions.arithmetic.ADDInstr
import generator.instructions.compare.CMPInstr
import generator.instructions.directives.LabelInstr
import generator.instructions.load.LDREQInstr
import generator.instructions.load.LDRNEInstr
import generator.instructions.move.MOVInstr
import generator.instructions.operands.LabelOp
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.translator.ArmConstants.NUM_BYTE_ADDRESS
import generator.translator.TranslatorContext
import generator.translator.lib.LibraryFunction

object PrintBool : LibraryFunction {
    override val label = "p_print_bool"
    private var trueIndex: Int? = null
    private var falseIndex: Int? = null

    override fun initIndex(ctx: TranslatorContext) {
        trueIndex = ctx.addMessage("true\\0")
        falseIndex = ctx.addMessage("false\\0")
    }

    override fun generateArm() =
        listOf(
            LabelInstr(label),
            FunctionStart(),
            CMPInstr(Register.R0, NumOp(0)),
            LDRNEInstr(Register.R0, LabelOp(trueIndex!!)),
            LDREQInstr(Register.R0, LabelOp(falseIndex!!)),
            ADDInstr(Register.R0, Register.R0, NumOp(NUM_BYTE_ADDRESS)),
            Syscall("printf"),
            MOVInstr(Register.R0, NumOp(0)),
            Syscall("fflush"),
            FunctionEnd()
        )

    override fun generatex86() =
        listOf(
            LabelInstr(label),
            FunctionStart(),
            CMPInstr(Register.R0, NumOp(0)),
            LDRNEInstr(Register.R0, LabelOp(trueIndex!!)),
            LDREQInstr(Register.R0, LabelOp(falseIndex!!)),
            Syscall("printf"),
            MOVInstr(Register.R0, NumOp(0)),
            Syscall("fflush"),
            FunctionEnd()
        )
}