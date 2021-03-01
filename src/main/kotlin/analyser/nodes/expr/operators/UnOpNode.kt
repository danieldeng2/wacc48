package analyser.nodes.expr.operators

import analyser.SymbolTable
import analyser.nodes.expr.ExprNode
import analyser.nodes.function.FuncNode
import analyser.nodes.type.*
import exceptions.SyntaxException
import generator.instructions.Instruction
import generator.instructions.arithmetic.RSBSInstr
import generator.instructions.branch.BLVSInstr
import generator.instructions.load.LDRInstr
import generator.instructions.logical.EORInstr
import generator.instructions.operands.MemAddr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.translator.TranslatorContext
import generator.translator.lib.errors.OverflowError
import org.antlr.v4.runtime.ParserRuleContext

data class UnOpNode(
    val operator: UnaryOperator,
    val expr: ExprNode,
    override val ctx: ParserRuleContext?,
) : ExprNode {
    override var type: Type = operator.returnType
    override lateinit var st: SymbolTable


    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.st = st
        expr.validate(st, funTable)

        if (expr.type !in operator.expectedExprTypes)
            throw SyntaxException(
                "Expression type for $operator " +
                        "does not match required type $type"
            )
    }

    override fun translate(ctx: TranslatorContext) =
        when (operator) {
            UnaryOperator.NEGATE -> translateNegate(ctx)
            UnaryOperator.CHR, UnaryOperator.ORD -> expr.translate(ctx)
            UnaryOperator.MINUS -> translateMinus(ctx)
            UnaryOperator.LEN -> mutableListOf<Instruction>().apply {
                addAll(expr.translate(ctx))
                add(LDRInstr(Register.R0, MemAddr(Register.R0)))
            }
        }

    private fun translateNegate(ctx: TranslatorContext) =
        mutableListOf<Instruction>().apply {
            addAll(expr.translate(ctx))
            add(EORInstr(Register.R0, Register.R0, NumOp(1)))
        }

    private fun translateMinus(ctx: TranslatorContext) =
        mutableListOf<Instruction>().apply {
            ctx.addLibraryFunction(OverflowError)
            addAll(expr.translate(ctx))
            add(RSBSInstr(Register.R0, Register.R0, NumOp(0)))
            add(BLVSInstr(OverflowError.label))
        }
}

enum class UnaryOperator(
    val repr: String, val expectedExprTypes: List<Type>, val returnType: Type
) {
    MINUS("-", listOf(IntType), IntType),
    NEGATE("!", listOf(BoolType), BoolType),
    LEN("len", listOf(StringType, ArrayType(VoidType, null)), IntType),
    ORD("ord", listOf(CharType), IntType),
    CHR("chr", listOf(IntType), CharType);

    companion object {
        fun lookupRepresentation(string: String) =
            values().firstOrNull { it.repr == string }
    }
}