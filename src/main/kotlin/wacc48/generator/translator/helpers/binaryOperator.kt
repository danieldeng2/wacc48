package wacc48.generator.translator.helpers

import wacc48.generator.instructions.arithmetic.ADDSInstr
import wacc48.generator.instructions.arithmetic.IDIVInstr
import wacc48.generator.instructions.arithmetic.MODInstr
import wacc48.generator.instructions.arithmetic.SMULLInstr
import wacc48.generator.instructions.arithmetic.SUBSInstr
import wacc48.generator.instructions.branch.BEQInstr
import wacc48.generator.instructions.branch.BLInstr
import wacc48.generator.instructions.branch.BLNEInstr
import wacc48.generator.instructions.branch.BLVSInstr
import wacc48.generator.instructions.compare.CMPInstr
import wacc48.generator.instructions.directives.LabelInstr
import wacc48.generator.instructions.move.MOVEQInstr
import wacc48.generator.instructions.move.MOVGEInstr
import wacc48.generator.instructions.move.MOVGTInstr
import wacc48.generator.instructions.move.MOVInstr
import wacc48.generator.instructions.move.MOVLEInstr
import wacc48.generator.instructions.move.MOVLTInstr
import wacc48.generator.instructions.move.MOVNEInstr
import wacc48.generator.instructions.operands.NumOp
import wacc48.generator.instructions.operands.Register
import wacc48.generator.instructions.operands.ShiftOp
import wacc48.generator.instructions.operands.ShiftType
import wacc48.generator.translator.CodeGeneratorVisitor
import wacc48.generator.translator.lib.errors.DivideByZeroError
import wacc48.generator.translator.lib.errors.OverflowError
import wacc48.tree.nodes.expr.operators.BinOpNode
import wacc48.tree.nodes.expr.operators.operation.binary.DivideOperation
import wacc48.tree.nodes.expr.operators.operation.binary.EqualsOperation
import wacc48.tree.nodes.expr.operators.operation.binary.GreaterEqualThanOperation
import wacc48.tree.nodes.expr.operators.operation.binary.GreaterThanOperation
import wacc48.tree.nodes.expr.operators.operation.binary.LessEqualThanOperation
import wacc48.tree.nodes.expr.operators.operation.binary.LessThanOperation
import wacc48.tree.nodes.expr.operators.operation.binary.MinusOperation
import wacc48.tree.nodes.expr.operators.operation.binary.ModulusOperation
import wacc48.tree.nodes.expr.operators.operation.binary.MultiplyOperation
import wacc48.tree.nodes.expr.operators.operation.binary.NotEqualsOperation
import wacc48.tree.nodes.expr.operators.operation.binary.PlusOperation


fun CodeGeneratorVisitor.translateAnd(node: BinOpNode) {
    visitNode(node.firstExpr)

    ctx.text.apply {
        add(CMPInstr(Register.R0, NumOp(0)))

        val branchFirstOp = ctx.labelCounter

        add(BEQInstr("L$branchFirstOp"))
        visitNode(node.secondExpr)

        add(LabelInstr("L$branchFirstOp"))
        add(CMPInstr(Register.R0, NumOp(0)))
    }
}

fun CodeGeneratorVisitor.translateOr(node: BinOpNode) {
    visitNode(node.firstExpr)

    ctx.text.apply {
        add(CMPInstr(Register.R0, NumOp(1)))


        val branchFirstOp = ctx.labelCounter

        add(BEQInstr("L$branchFirstOp"))
        visitNode(node.secondExpr)

        add(LabelInstr("L$branchFirstOp"))
    }
}


val binaryInstructions = mapOf(
    EqualsOperation to listOf(
        CMPInstr(Register.R0, Register.R1),
        MOVEQInstr(Register.R0, NumOp(1)),
        MOVNEInstr(Register.R0, NumOp(0))
    ),

    NotEqualsOperation to listOf(
        CMPInstr(Register.R0, Register.R1),
        MOVNEInstr(Register.R0, NumOp(1)),
        MOVEQInstr(Register.R0, NumOp(0))
    ),

    PlusOperation to listOf(
        ADDSInstr(Register.R0, Register.R0, Register.R1),
        BLVSInstr(OverflowError.label),
    ),

    MinusOperation to listOf(
        SUBSInstr(Register.R0, Register.R0, Register.R1),
        BLVSInstr(OverflowError.label),
    ),

    MultiplyOperation to listOf(
        SMULLInstr(Register.R0, Register.R1, Register.R0, Register.R1),
        CMPInstr(
            Register.R1,
            ShiftOp(Register.R0, ShiftType.ASR, NumOp(31))
        ),
        BLNEInstr(OverflowError.label)
    ),

    DivideOperation to listOf(
        BLInstr(DivideByZeroError.label),
        IDIVInstr()
    ),

    ModulusOperation to listOf(
        BLInstr(DivideByZeroError.label),
        MODInstr(),
        MOVInstr(Register.R0, Register.R1)
    ),
    GreaterThanOperation to listOf(
        CMPInstr(Register.R0, Register.R1),
        MOVGTInstr(Register.R0, NumOp(1)),
        MOVLEInstr(Register.R0, NumOp(0))
    ),
    GreaterEqualThanOperation to listOf(
        CMPInstr(Register.R0, Register.R1),
        MOVGEInstr(Register.R0, NumOp(1)),
        MOVLTInstr(Register.R0, NumOp(0))
    ),
    LessThanOperation to listOf(
        CMPInstr(Register.R0, Register.R1),
        MOVLTInstr(Register.R0, NumOp(1)),
        MOVGEInstr(Register.R0, NumOp(0))
    ),
    LessEqualThanOperation to listOf(
        CMPInstr(Register.R0, Register.R1),
        MOVLEInstr(Register.R0, NumOp(1)),
        MOVGTInstr(Register.R0, NumOp(0))
    )
)

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