package tree.nodes.function

import tree.SymbolTable
import tree.nodes.ASTNode
import tree.nodes.expr.ExprNode
import org.antlr.v4.runtime.ParserRuleContext
import tree.ASTVisitor

data class ArgListNode(
    var args: List<ExprNode>,
    val ctx: ParserRuleContext?
) : ASTNode {


    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        args.forEach {
            it.validate(st, funTable)
        }
    }

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitArgList(this)
    }
}