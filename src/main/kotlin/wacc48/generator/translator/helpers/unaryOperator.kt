package wacc48.generator.translator.helpers

import wacc48.generator.instructions.arithmetic.RSBSInstr
import wacc48.generator.instructions.branch.BLVSInstr
import wacc48.generator.instructions.logical.EORInstr
import wacc48.generator.instructions.operands.NumOp
import wacc48.generator.instructions.operands.Register
import wacc48.generator.translator.CodeGeneratorVisitor
import wacc48.generator.translator.lib.errors.OverflowError
import wacc48.tree.nodes.expr.operators.UnOpNode


fun CodeGeneratorVisitor.translateNegate(node: UnOpNode) {
    visitNode(node.expr)
    ctx.text.apply {
        add(EORInstr(Register.R0, Register.R0, NumOp(1)))
    }
}

fun CodeGeneratorVisitor.translateMinus(node: UnOpNode) {
    ctx.addLibraryFunction(OverflowError)
    visitNode(node.expr)

    ctx.text.apply {
        add(RSBSInstr(Register.R0, Register.R0, NumOp(0)))
        add(BLVSInstr(OverflowError.label))
    }
}

