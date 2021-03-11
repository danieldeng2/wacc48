package generator.translator.lib.errors

import generator.instructions.Instruction
import generator.instructions.Syscall
import generator.instructions.branch.BLInstr
import generator.instructions.directives.LabelInstr
import generator.instructions.move.MOVInstr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.translator.TranslatorContext
import generator.translator.lib.LibraryFunction
import generator.translator.lib.print.PrintStr

object RuntimeError : LibraryFunction {
    override val label = "p_throw_runtime_error"

    override fun generateArm() = mutableListOf<Instruction>().apply {
        add(LabelInstr(label))
        add(BLInstr(PrintStr.label))
        add(MOVInstr(Register.R0, NumOp(-1)))
        add(Syscall("exit"))
    }

    override fun initIndex(ctx: TranslatorContext) {
        ctx.addLibraryFunction(PrintStr)
    }

}
