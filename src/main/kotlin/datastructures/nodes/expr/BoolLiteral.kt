package datastructures.nodes.expr

import datastructures.SymbolTable
import datastructures.nodes.function.FuncNode
import datastructures.type.BoolType
import datastructures.type.Type
import generator.instructions.move.MOVInstr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.translator.CodeGeneratorVisitor
import generator.translator.TranslatorContext
import org.antlr.v4.runtime.ParserRuleContext

data class BoolLiteral(
    val value: Boolean,
    val ctx: ParserRuleContext?
) : ExprNode {
    override var type: Type = BoolType

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
    }

    override fun translate(ctx: TranslatorContext) =
        when (value) {
            true -> listOf(MOVInstr(Register.R0, NumOp(1)))
            false -> listOf(MOVInstr(Register.R0, NumOp(0)))
        }

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateBoolLiteral(this)
    }
}