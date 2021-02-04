package analyser.nodes.function

import analyser.SymbolTable
import analyser.nodes.ASTNode
import analyser.nodes.expr.ExprNode

data class ArgListNode(
    private val args: List<ExprNode>
) : ASTNode {
    override fun validate(st: SymbolTable) {
        args.forEach {
            it.validate(st)
        }
    }
}