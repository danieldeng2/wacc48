package datastructures.nodes.expr

import datastructures.SymbolTable
import datastructures.nodes.function.FuncNode
import datastructures.type.StringType
import datastructures.type.Type
import generator.translator.CodeGeneratorVisitor
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

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateStringLiteral(this)
    }
}