package tree.nodes.statement

import tree.SymbolTable
import tree.nodes.expr.ExprNode
import tree.nodes.function.FuncNode
import org.antlr.v4.runtime.ParserRuleContext
import tree.ASTVisitor

data class ReturnNode(
    val value: ExprNode,
    val ctx: ParserRuleContext?,
) : StatNode {
    lateinit var st: SymbolTable

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.st = st
        value.validate(st, funTable)
    }

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitReturn(this)
    }
}