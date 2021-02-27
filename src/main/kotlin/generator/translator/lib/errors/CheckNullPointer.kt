package generator.translator.lib.errors

import generator.instructions.Instruction
import generator.instructions.branch.BLEQInstr
import generator.instructions.compare.CMPInstr
import generator.instructions.directives.LabelInstr
import generator.instructions.load.LDREQInstr
import generator.instructions.operands.LabelOp
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.instructions.stack.POPInstr
import generator.instructions.stack.PUSHInstr
import generator.translator.TranslatorContext
import generator.translator.lib.LibaryFunction

object CheckNullPointer : LibaryFunction {
    private var msgIndex: Int? = null
    override val label = "p_check_null_pointer"

    override fun translate() = listOf(
        LabelInstr(label),
        PUSHInstr(Register.LR),
        CMPInstr(Register.R0, NumOp(0)),
        LDREQInstr(Register.R0, LabelOp(msgIndex!!)),
        BLEQInstr(RuntimeError.label),
        POPInstr(Register.PC)
    )

    override fun initIndex(ctx: TranslatorContext) {
        ctx.addLibraryFunction(RuntimeError)

        msgIndex = ctx.addMessage(
            "NullReferenceError: dereference a null reference\\n\\0"
        )
    }

}
