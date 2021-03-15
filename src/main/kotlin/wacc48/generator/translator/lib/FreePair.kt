package wacc48.generator.translator.lib

import wacc48.generator.instructions.FunctionEnd
import wacc48.generator.instructions.FunctionStart
import wacc48.generator.instructions.Syscall
import wacc48.generator.instructions.branch.BEQInstr
import wacc48.generator.instructions.compare.CMPInstr
import wacc48.generator.instructions.directives.LabelInstr
import wacc48.generator.instructions.load.LDREQInstr
import wacc48.generator.instructions.load.LDRInstr
import wacc48.generator.instructions.operands.LabelOp
import wacc48.generator.instructions.operands.MemAddr
import wacc48.generator.instructions.operands.NumOp
import wacc48.generator.instructions.operands.Register
import wacc48.generator.instructions.stack.POPInstr
import wacc48.generator.instructions.stack.PUSHInstr
import wacc48.generator.translator.ArmConstants.NUM_BYTE_ADDRESS
import wacc48.generator.translator.TranslatorContext
import wacc48.generator.translator.lib.errors.RuntimeError

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
