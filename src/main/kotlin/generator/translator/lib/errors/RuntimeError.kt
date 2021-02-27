package generator.translator.lib.errors

import generator.instructions.branch.BLInstr
import generator.instructions.Instruction
import generator.instructions.directives.LabelInstr
import generator.instructions.move.MOVInstr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.translator.TranslatorContext
import generator.translator.lib.print.PrintStr
import generator.translator.lib.LibraryFunction

object RuntimeError : LibraryFunction {
    override val label = "p_throw_runtime_error"

    override fun translate() = mutableListOf<Instruction>().apply {
        add(LabelInstr(label))
        add(BLInstr(PrintStr.label))
        add(MOVInstr(Register.R0, NumOp(-1)))
        add(BLInstr("exit"))
    }

    override fun initIndex(ctx: TranslatorContext) {
        ctx.addLibraryFunction(PrintStr)
    }

}
