package wacc48.generator.translator.helpers

import wacc48.generator.instructions.arithmetic.*
import wacc48.generator.instructions.branch.BEQInstr
import wacc48.generator.instructions.branch.BLInstr
import wacc48.generator.instructions.branch.BLNEInstr
import wacc48.generator.instructions.branch.BLVSInstr
import wacc48.generator.instructions.compare.CMPInstr
import wacc48.generator.instructions.directives.LabelInstr
import wacc48.generator.instructions.move.*
import wacc48.generator.instructions.operands.NumOp
import wacc48.generator.instructions.operands.Register
import wacc48.generator.instructions.operands.ShiftOp
import wacc48.generator.instructions.operands.ShiftType
import wacc48.generator.translator.CodeGeneratorVisitor
import wacc48.generator.translator.lib.errors.DivideByZeroError
import wacc48.generator.translator.lib.errors.OverflowError
import wacc48.tree.nodes.expr.operators.BinOpNode
import wacc48.tree.nodes.expr.operators.BinaryOperator
import java.rmi.UnexpectedException


fun CodeGeneratorVisitor.translateEquality(node: BinOpNode, isEqual: Boolean) {
    loadOperandsIntoRegister(node)

    ctx.text.apply {

        add(CMPInstr(Register.R0, Register.R1))

        if (isEqual) {
            add(MOVEQInstr(Register.R0, NumOp(1)))
            add(MOVNEInstr(Register.R0, NumOp(0)))
        } else {
            add(MOVNEInstr(Register.R0, NumOp(1)))
            add(MOVEQInstr(Register.R0, NumOp(0)))
        }
    }
}

fun CodeGeneratorVisitor.translateMultiply(node: BinOpNode) {
    ctx.addLibraryFunction(OverflowError)
    loadOperandsIntoRegister(node)

    ctx.text.apply {
        add(SMULLInstr(Register.R0, Register.R1, Register.R0, Register.R1))
        add(
            CMPInstr(
                Register.R1,
                ShiftOp(Register.R0, ShiftType.ASR, NumOp(31))
            )
        )
        add(BLNEInstr(OverflowError.label))
    }
}

fun CodeGeneratorVisitor.translateModulo(node: BinOpNode) {
    ctx.addLibraryFunction(DivideByZeroError)
    loadOperandsIntoRegister(node)

    ctx.text.apply {
        add(BLInstr(DivideByZeroError.label))
        add(MODInstr())
        add(MOVInstr(Register.R0, Register.R1))
    }
}


fun CodeGeneratorVisitor.translateDivide(node: BinOpNode) {
    ctx.addLibraryFunction(DivideByZeroError)
    loadOperandsIntoRegister(node)

    ctx.text.apply {
        add(BLInstr(DivideByZeroError.label))
        add(IDIVInstr())
    }
}

fun CodeGeneratorVisitor.translatePlusMinus(node: BinOpNode, isPlus: Boolean) {
    ctx.addLibraryFunction(OverflowError)
    loadOperandsIntoRegister(node)

    ctx.text.apply {
        if (isPlus)
            add(ADDSInstr(Register.R0, Register.R0, Register.R1))
        else
            add(SUBSInstr(Register.R0, Register.R0, Register.R1))

        add(BLVSInstr(OverflowError.label))
    }
}

fun CodeGeneratorVisitor.translateLogical(node: BinOpNode, isAnd: Boolean) {
    visitNode(node.firstExpr)

    ctx.text.add(
        if (isAnd)
            CMPInstr(Register.R0, NumOp(0))
        else
            CMPInstr(Register.R0, NumOp(1))
    )

    val branchFirstOp = ctx.labelCounter

    ctx.text.add(BEQInstr("L$branchFirstOp"))
    visitNode(node.secondExpr)

    ctx.text.add(LabelInstr("L$branchFirstOp"))

    if (isAnd)
        ctx.text.add(CMPInstr(Register.R0, NumOp(0)))

}

fun CodeGeneratorVisitor.translateComparator(node: BinOpNode) {
    loadOperandsIntoRegister(node)
    ctx.text.apply {

        add(CMPInstr(Register.R0, Register.R1))

        when (node.operator) {
            BinaryOperator.GT -> {
                add(MOVGTInstr(Register.R0, NumOp(1)))
                add(MOVLEInstr(Register.R0, NumOp(0)))
            }

            BinaryOperator.GE -> {
                add(MOVGEInstr(Register.R0, NumOp(1)))
                add(MOVLTInstr(Register.R0, NumOp(0)))
            }

            BinaryOperator.LT -> {
                add(MOVLTInstr(Register.R0, NumOp(1)))
                add(MOVGEInstr(Register.R0, NumOp(0)))
            }

            BinaryOperator.LE -> {
                add(MOVLEInstr(Register.R0, NumOp(1)))
                add(MOVGTInstr(Register.R0, NumOp(0)))
            }

            else -> throw UnexpectedException(
                "Unexpected fall through to 'else' branch in " +
                        "${object {}.javaClass.enclosingMethod.name} with operator ${node.operator}"
            )
        }

    }
}

/** Loads the first and second operands in a binary operator into R0 and R1
 * registers. */
fun CodeGeneratorVisitor.loadOperandsIntoRegister(node: BinOpNode) {
    ctx.addLibraryFunction(DivideByZeroError)
    node.firstExpr.acceptVisitor(this)

    ctx.text.add(pushAndIncrement(ctx, Register.R0))
    node.secondExpr.acceptVisitor(this)

    ctx.text.apply {
        add(MOVInstr(Register.R1, Register.R0))
        add(popAndDecrement(ctx, Register.R0))
    }
}