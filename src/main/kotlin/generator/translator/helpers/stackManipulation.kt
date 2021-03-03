package generator.translator.helpers

import tree.nodes.expr.operators.BinOpNode
import generator.instructions.move.MOVInstr
import generator.instructions.operands.Register
import generator.instructions.stack.POPInstr
import generator.instructions.stack.PUSHInstr
import generator.translator.ArmConstants.NUM_BYTE_ADDRESS
import generator.translator.CodeGeneratorVisitor
import generator.translator.TranslatorContext
import generator.translator.lib.errors.DivideByZeroError


fun pushAndIncrement(
    ctx: TranslatorContext,
    vararg registers: Register
): PUSHInstr {
    ctx.stackPtrOffset += NUM_BYTE_ADDRESS * registers.size
    return PUSHInstr(*registers)
}

fun popAndDecrement(
    ctx: TranslatorContext,
    vararg registers: Register
): POPInstr {
    ctx.stackPtrOffset -= NUM_BYTE_ADDRESS * registers.size
    return POPInstr(*registers)
}

fun CodeGeneratorVisitor.loadOperandsIntoRegister(node: BinOpNode) {
    ctx.addLibraryFunction(DivideByZeroError)
    node.firstExpr.acceptCodeGenVisitor(this)

    ctx.text.add(pushAndIncrement(ctx, Register.R0))
    node.secondExpr.acceptCodeGenVisitor(this)

    ctx.text.apply {
        add(MOVInstr(Register.R1, Register.R0))
        add(popAndDecrement(ctx, Register.R0))
    }
}