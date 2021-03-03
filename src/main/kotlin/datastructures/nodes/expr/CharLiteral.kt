package datastructures.nodes.expr

import datastructures.SymbolTable
import datastructures.nodes.function.FuncNode
import datastructures.type.CharType
import datastructures.type.Type
import generator.translator.CodeGeneratorVisitor
import org.antlr.v4.runtime.ParserRuleContext

data class CharLiteral(val value: Char, val ctx: ParserRuleContext?) :
    ExprNode {
    override var type: Type = CharType

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
    }

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateCharLiteral(this)
    }

}