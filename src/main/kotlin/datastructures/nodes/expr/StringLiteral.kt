package datastructures.nodes.expr

import datastructures.SymbolTable
import datastructures.nodes.function.FuncNode
import datastructures.type.StringType
import datastructures.type.Type
import generator.instructions.load.LDRInstr
import generator.instructions.operands.LabelOp
import generator.instructions.operands.Register
import generator.translator.CodeGeneratorVisitor
import generator.translator.TranslatorContext
import org.antlr.v4.runtime.ParserRuleContext


data class StringLiteral(
    val value: String,
    val ctx: ParserRuleContext?
) : ExprNode {


    override var type: Type = StringType

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
    }

    override fun translate(ctx: TranslatorContext) = listOf(
        LDRInstr(
            Register.R0,
            LabelOp(ctx.addMessage(value))
        )
    )

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateStringLiteral(this)
    }
}