package tree.nodes.expr

import tree.SymbolTable
import tree.nodes.function.FuncNode
import tree.type.CharType
import tree.type.Type
import org.antlr.v4.runtime.ParserRuleContext
import tree.ASTVisitor

data class CharLiteral(val value: Char, val ctx: ParserRuleContext?) :
    ExprNode {
    override var type: Type = CharType

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
    }

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitCharLiteral(this)
    }

}