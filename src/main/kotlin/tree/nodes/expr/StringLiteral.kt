package tree.nodes.expr

import tree.SymbolTable
import tree.nodes.function.FuncNode
import tree.type.StringType
import tree.type.Type
import org.antlr.v4.runtime.ParserRuleContext
import tree.ASTVisitor


data class StringLiteral(
    val value: String,
    val ctx: ParserRuleContext?
) : ExprNode, BaseLiteral {


    override var type: Type = StringType

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
    }

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitStringLiteral(this)
    }
}