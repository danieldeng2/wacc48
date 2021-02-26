package generator.translator.errors

import generator.instructions.BLInstr
import generator.instructions.Instruction
import generator.instructions.LabelInstr
import generator.instructions.MOVInstr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.translator.TranslatorContext
import generator.translator.print.PrintStr
import generator.translator.print.PrintSyscall

object RuntimeError : PrintSyscall {
    override val label = "p_throw_runtime_error"

    override fun translate() = mutableListOf<Instruction>().apply {
        add(LabelInstr(label))
        add(BLInstr(PrintStr.label))
        add(MOVInstr(Register.R0, NumOp(-1)))
        add(BLInstr("exit"))
    }

    override fun initIndex(ctx: TranslatorContext) {
        ctx.addPrintFunc(PrintStr)
    }

}
