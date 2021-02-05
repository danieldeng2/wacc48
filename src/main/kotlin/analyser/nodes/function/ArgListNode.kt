package analyser.nodes.function

import analyser.SymbolTable
import analyser.nodes.ASTNode
import analyser.nodes.expr.ExprNode

data class ArgListNode(
    val args: List<ExprNode>
) : ASTNode {
    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        args.forEach {
            it.validate(st, funTable)
        }
    }
}