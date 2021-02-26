package generator.translator.lib

import generator.instructions.Instruction
import generator.instructions.branch.BEQInstr
import generator.instructions.branch.BLInstr
import generator.instructions.compare.CMPInstr
import generator.instructions.directives.LabelInstr
import generator.instructions.load.LDREQInstr
import generator.instructions.load.LDRInstr
import generator.instructions.operands.LabelOp
import generator.instructions.operands.MemAddr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.instructions.stack.POPInstr
import generator.instructions.stack.PUSHInstr
import generator.translator.TranslatorContext
import generator.translator.lib.errors.RuntimeError

object FreePair : LibaryFunction {
    private var msgIndex: Int? = null
    override val label = "p_free_pair"

    override fun translate() = listOf(
        LabelInstr(label),
        PUSHInstr(Register.LR),

        // Check for null dereferencing
        CMPInstr(Register.R0, NumOp(0)),
        LDREQInstr(Register.R0, LabelOp(msgIndex!!)),
        BEQInstr(RuntimeError.label),

        // Free fst element
        PUSHInstr(Register.R0),
        LDRInstr(Register.R0, MemAddr(Register.R0)),
        BLInstr("free"),

        // Free snd element
        LDRInstr(Register.R0, MemAddr(Register.SP)),
        LDRInstr(Register.R0, MemAddr(Register.R0, NumOp(4))),
        BLInstr("free"),

        // Free the pair object
        POPInstr(Register.R0),
        BLInstr("free"),

        POPInstr(Register.PC)
    )

    override fun initIndex(ctx: TranslatorContext) {
        ctx.addLibraryFunction(RuntimeError)

        msgIndex = ctx.addMessage(
            "NullreferenceError: dereference a null reference\\n\\0"
        )
    }

}
