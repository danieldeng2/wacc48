package tree.nodes.statement

import tree.SymbolTable
import tree.nodes.expr.ExprNode
import tree.nodes.function.FuncNode
import org.antlr.v4.runtime.ParserRuleContext
import tree.ASTVisitor

data class PrintNode(
    var value: ExprNode,
    val returnAfterPrint: Boolean = false,
    val ctx: ParserRuleContext?,
) : StatNode {


    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {

        value.validate(st, funTable)
    }

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitPrint(this)
    }
}