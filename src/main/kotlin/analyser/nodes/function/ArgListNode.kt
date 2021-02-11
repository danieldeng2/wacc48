package analyser.nodes.function

import analyser.SymbolTable
import analyser.nodes.ASTNode
import analyser.nodes.expr.ExprNode
import org.antlr.v4.runtime.ParserRuleContext

data class ArgListNode(
    val args: List<ExprNode>,
    override val ctx: ParserRuleContext?
) : ASTNode {
    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        args.forEach {
            it.validate(st, funTable)
        }
    }
}