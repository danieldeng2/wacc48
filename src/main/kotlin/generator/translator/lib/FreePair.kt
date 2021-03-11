package generator.translator.lib

import generator.instructions.FunctionEnd
import generator.instructions.FunctionStart
import generator.instructions.Instruction
import generator.instructions.Syscall
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
import generator.translator.ArmConstants.NUM_BYTE_ADDRESS
import generator.translator.TranslatorContext
import generator.translator.lib.errors.RuntimeError

object FreePair : LibraryFunction {
    private var msgIndex: Int? = null
    override val label = "p_free_pair"

    override fun generateArm() = listOf(
        LabelInstr(label),
        FunctionStart(),

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
        LDRInstr(Register.R0, MemAddr(Register.R0, NumOp(NUM_BYTE_ADDRESS))),
        BLInstr("free"),

        // Free the pair object
        POPInstr(Register.R0),
        BLInstr("free"),

        FunctionEnd()
    )

    override fun generatex86() = listOf(
        LabelInstr(label),
        FunctionStart(),

        // Check for null dereferencing
        CMPInstr(Register.R0, NumOp(0)),
        LDREQInstr(Register.R0, LabelOp(msgIndex!!)),
        BEQInstr(RuntimeError.label),

        // Free fst element
        PUSHInstr(Register.R0),
        LDRInstr(Register.R0, MemAddr(Register.R0)),
        Syscall("free"),

        // Free snd element
        LDRInstr(Register.R0, MemAddr(Register.SP)),
        LDRInstr(Register.R0, MemAddr(Register.R0, NumOp(NUM_BYTE_ADDRESS))),
        Syscall("free"),

        // Free the pair object
        POPInstr(Register.R0),
        Syscall("free"),

        FunctionEnd()
    )

    override fun initIndex(ctx: TranslatorContext) {
        ctx.addLibraryFunction(RuntimeError)

        msgIndex = ctx.addMessage(
            "NullReferenceError: dereference a null reference\\n\\0"
        )
    }
}
