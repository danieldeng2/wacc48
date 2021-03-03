package generator.translator.helpers

import datastructures.nodes.expr.operators.UnOpNode
import generator.instructions.arithmetic.RSBSInstr
import generator.instructions.branch.BLVSInstr
import generator.instructions.logical.EORInstr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.translator.CodeGeneratorVisitor
import generator.translator.lib.errors.OverflowError


fun CodeGeneratorVisitor.translateNegate(node: UnOpNode) {
    visitAndTranslate(node.expr)
    ctx.text.apply {
        add(EORInstr(Register.R0, Register.R0, NumOp(1)))
    }
}

fun CodeGeneratorVisitor.translateMinus(node: UnOpNode) {
    ctx.addLibraryFunction(OverflowError)
    visitAndTranslate(node.expr)

    ctx.text.apply {
        add(RSBSInstr(Register.R0, Register.R0, NumOp(0)))
        add(BLVSInstr(OverflowError.label))
    }
}

