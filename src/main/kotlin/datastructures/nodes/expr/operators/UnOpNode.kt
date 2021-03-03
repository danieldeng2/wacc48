package datastructures.nodes.expr.operators

import datastructures.SymbolTable
import datastructures.nodes.expr.ExprNode
import datastructures.nodes.function.FuncNode
import datastructures.type.*
import analyser.exceptions.SyntaxException
import generator.instructions.Instruction
import generator.instructions.arithmetic.RSBSInstr
import generator.instructions.branch.BLVSInstr
import generator.instructions.load.LDRInstr
import generator.instructions.logical.EORInstr
import generator.instructions.operands.MemAddr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.translator.CodeGeneratorVisitor
import generator.translator.TranslatorContext
import generator.translator.lib.errors.OverflowError
import org.antlr.v4.runtime.ParserRuleContext

data class UnOpNode(
    val operator: UnaryOperator,
    val expr: ExprNode,
    val ctx: ParserRuleContext?,
) : ExprNode {
    override var type: Type = operator.returnType


    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {

        expr.validate(st, funTable)

        if (expr.type !in operator.expectedExprTypes)
            throw SyntaxException(
                "Expression type for $operator " +
                        "does not match required type $type"
            )
    }


    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateUnOp(this)
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