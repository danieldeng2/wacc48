package wacc48.tree.nodes.function

import org.antlr.v4.runtime.ParserRuleContext
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.ASTNode
import wacc48.tree.nodes.expr.ExprNode

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