package wacc48.generator.translator.lib.print

import wacc48.generator.instructions.FunctionEnd
import wacc48.generator.instructions.FunctionStart
import wacc48.generator.instructions.Syscall
import wacc48.generator.instructions.arithmetic.ADDInstr
import wacc48.generator.instructions.branch.BEQInstr
import wacc48.generator.instructions.branch.BInstr
import wacc48.generator.instructions.compare.CMPInstr
import wacc48.generator.instructions.directives.LabelInstr
import wacc48.generator.instructions.load.LDREQInstr
import wacc48.generator.instructions.load.LDRNEInstr
import wacc48.generator.instructions.move.MOVInstr
import wacc48.generator.instructions.operands.LabelOp
import wacc48.generator.instructions.operands.NumOp
import wacc48.generator.instructions.operands.Register
import wacc48.generator.instructions.stack.PUSHInstr
import wacc48.generator.translator.ArmConstants.NUM_BYTE_ADDRESS
import wacc48.generator.translator.TranslatorContext
import wacc48.generator.translator.lib.LibraryFunction

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
            PUSHInstr(Register.R0),
            CMPInstr(Register.R0, NumOp(0)),
            BEQInstr("p_print_false"),
            MOVInstr(Register.R0, LabelOp(trueIndex!!)),
            BInstr("p_print_bool_continue"),
            LabelInstr("p_print_false"),
            MOVInstr(Register.R0, LabelOp(falseIndex!!)),
            LabelInstr("p_print_bool_continue"),
            Syscall("printf"),
            ADDInstr(Register.SP, Register.SP, NumOp(4)),
            MOVInstr(Register.R0, NumOp(0)),
            Syscall("fflush"),
            FunctionEnd()
        )
}