package generator.translator.lib.read

import generator.instructions.FunctionEnd
import generator.instructions.FunctionStart
import generator.instructions.Instruction
import generator.instructions.Syscall
import generator.instructions.arithmetic.ADDInstr
import generator.instructions.directives.LabelInstr
import generator.instructions.load.LDRInstr
import generator.instructions.move.MOVInstr
import generator.instructions.operands.LabelOp
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.instructions.stack.PUSHInstr
import generator.translator.ArmConstants.NUM_BYTE_ADDRESS
import generator.translator.TranslatorContext
import generator.translator.lib.LibraryFunction

object ReadChar : LibraryFunction {
    override val label: String = "p_read_char"
    private var msgIndex: Int? = null

    override fun initIndex(ctx: TranslatorContext) {
        msgIndex = ctx.addMessage(" %c\\0")
    }

    override fun generateArm(): List<Instruction> =
        listOf(
            LabelInstr(label),
            FunctionStart(),
            MOVInstr(Register.R1, Register.R0),
            LDRInstr(Register.R0, LabelOp(msgIndex!!)),
            ADDInstr(Register.R0, Register.R0, NumOp(NUM_BYTE_ADDRESS)),
            Syscall("scanf"),
            FunctionEnd()
        )

    override fun generatex86(): List<Instruction> =
        listOf(
            LabelInstr(label),
            FunctionStart(),
            PUSHInstr(Register.R0),
            LDRInstr(Register.R0, LabelOp(msgIndex!!)),
            ADDInstr(Register.SP, Register.SP, NumOp(NUM_BYTE_ADDRESS)),
            Syscall("scanf"),
            FunctionEnd()
        )

}