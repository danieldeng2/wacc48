package analyser.nodes.expr.operators

import analyser.SymbolTable
import analyser.nodes.expr.ExprNode
import analyser.nodes.type.*
import exceptions.SemanticsException
import generator.translator.TranslatorContext
import generator.instructions.*
import generator.instructions.arithmetic.ADDSInstr
import generator.instructions.arithmetic.SMULLInstr
import generator.instructions.arithmetic.SUBSInstr
import generator.instructions.branch.BEQInstr
import generator.instructions.branch.BLInstr
import generator.instructions.branch.BLNEInstr
import generator.instructions.branch.BLVSInstr
import generator.instructions.compare.CMPInstr
import generator.instructions.directives.LabelInstr
import generator.instructions.move.MOVEQInstr
import generator.instructions.move.MOVInstr
import generator.instructions.move.MOVNEInstr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.instructions.operands.ShiftOp
import generator.translator.lib.errors.DivideByZeroError
import generator.translator.lib.errors.OverflowError
import generator.translator.popAndDecrement
import generator.translator.pushAndIncrement
import org.antlr.v4.runtime.ParserRuleContext

data class BinOpNode(
    val operator: BinaryOperator,
    val firstExpr: ExprNode,
    val secondExpr: ExprNode,
    override val ctx: ParserRuleContext?
) : ExprNode {
    override var type: Type = operator.returnType
    override lateinit var st: SymbolTable
    override lateinit var funTable: SymbolTable

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        this.st = st
        this.funTable = funTable
        firstExpr.validate(st, funTable)
        secondExpr.validate(st, funTable)

        val expected = operator.expectedExprTypes

        if (expected.isNotEmpty()) {
            if (firstExpr.type !in expected)
                throw SemanticsException(
                    "Type-mismatched on operator $operator: arg 1 has type " +
                            "${firstExpr.type}, required 1 of type(s) $expected",
                    ctx
                )
        }
        if (firstExpr.type != secondExpr.type)
            throw SemanticsException(
                "Type-mismatched on operator $operator: arg 1 has type " +
                        "${firstExpr.type}, arg 2 of type(s) ${secondExpr.type}",
                ctx
            )
    }

    override fun translate(ctx: TranslatorContext) =
        when (operator) {
            BinaryOperator.EQ -> translateEquality(ctx, isEqual = true)
            BinaryOperator.NEQ -> translateEquality(ctx, isEqual = false)
            BinaryOperator.AND -> translateLogical(ctx, isAnd = true)
            BinaryOperator.OR -> translateLogical(ctx, isAnd = false)
            BinaryOperator.PLUS -> translatePlusMinus(ctx, isPlus = true)
            BinaryOperator.MINUS -> translatePlusMinus(ctx, isPlus = false)
            BinaryOperator.MULTIPLY -> translateMultiply(ctx)
            BinaryOperator.DIVIDE -> translateDivide(ctx)
            else -> emptyList()
        }

    private fun translateDivide(ctx: TranslatorContext) =
        mutableListOf<Instruction>().apply {
            ctx.addPrintFunc(DivideByZeroError)

            addAll(firstExpr.translate(ctx))
            add(pushAndIncrement(Register.R0, ctx))

            addAll(secondExpr.translate(ctx))
            add(MOVInstr(Register.R1, Register.R0))
            add(popAndDecrement(Register.R0, ctx))

            add(BLInstr(DivideByZeroError.label))
            add(BLInstr("__aeabi_idiv"))
        }

    private fun translateMultiply(ctx: TranslatorContext) =
        mutableListOf<Instruction>().apply {
            ctx.addPrintFunc(OverflowError)

            addAll(firstExpr.translate(ctx))
            add(pushAndIncrement(Register.R0, ctx))

            addAll(secondExpr.translate(ctx))
            add(MOVInstr(Register.R1, Register.R0))
            add(popAndDecrement(Register.R0, ctx))

            add(SMULLInstr(Register.R0, Register.R1, Register.R0, Register.R1))
            add(CMPInstr(Register.R1, ShiftOp(Register.R0, NumOp(31))))
            add(BLNEInstr(OverflowError.label))
        }

    private fun translatePlusMinus(ctx: TranslatorContext, isPlus: Boolean) =
        mutableListOf<Instruction>().apply {
            ctx.addPrintFunc(OverflowError)

            addAll(firstExpr.translate(ctx))
            add(pushAndIncrement(Register.R0, ctx))

            addAll(secondExpr.translate(ctx))
            add(MOVInstr(Register.R1, Register.R0))
            add(popAndDecrement(Register.R0, ctx))

            if (isPlus)
                add(ADDSInstr(Register.R0, Register.R0, Register.R1))
            else
                add(SUBSInstr(Register.R0, Register.R0, Register.R1))

            add(BLVSInstr(OverflowError.label))
        }

    private fun translateEquality(ctx: TranslatorContext, isEqual: Boolean) =
        mutableListOf<Instruction>().apply {
            addAll(firstExpr.translate(ctx))
            add(pushAndIncrement(Register.R0, ctx))

            addAll(secondExpr.translate(ctx))
            add(MOVInstr(Register.R1, Register.R0))
            add(popAndDecrement(Register.R0, ctx))

            add(CMPInstr(Register.R0, Register.R1))

            if (isEqual) {
                add(MOVEQInstr(Register.R0, NumOp(1)))
                add(MOVNEInstr(Register.R0, NumOp(0)))
            } else {
                add(MOVNEInstr(Register.R0, NumOp(1)))
                add(MOVEQInstr(Register.R0, NumOp(0)))
            }
        }

    private fun translateLogical(
        ctx: TranslatorContext,
        isAnd: Boolean
    ): List<Instruction> =
        mutableListOf<Instruction>().apply {
            addAll(firstExpr.translate(ctx))

            if (isAnd)
                add(CMPInstr(Register.R0, NumOp(0)))
            else
                add(CMPInstr(Register.R0, NumOp(1)))

            val branchFirstOp = ctx.labelCounter
            add(BEQInstr("L$branchFirstOp"))
            addAll(secondExpr.translate(ctx))

            add(LabelInstr("L$branchFirstOp"))
            if (isAnd)
                add(CMPInstr(Register.R0, NumOp(0)))

        }
}


enum class BinaryOperator(
    val repr: String,
    val expectedExprTypes: List<Type>,
    val returnType: Type
) {
    PLUS("+", listOf(IntType), IntType),
    MINUS("-", listOf(IntType), IntType),
    MULTIPLY("*", listOf(IntType), IntType),
    DIVIDE("/", listOf(IntType), IntType),
    MODULUS("%", listOf(IntType), IntType),
    GT(">", listOf(IntType, CharType), BoolType),
    LT("<", listOf(IntType, CharType), BoolType),
    GE(">=", listOf(IntType, CharType), BoolType),
    LE("<=", listOf(IntType, CharType), BoolType),
    EQ("==", emptyList(), BoolType),
    NEQ("!=", emptyList(), BoolType),
    AND("&&", listOf(BoolType), BoolType),
    OR("||", listOf(BoolType), BoolType);

    companion object {
        fun lookupRepresentation(string: String) =
            values().firstOrNull { it.repr == string }

    }

}