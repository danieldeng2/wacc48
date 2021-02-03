package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.expr.ExprNode

data class ReadNode(
    private val value: ExprNode,
) : StatNode {

    override fun validate(st: SymbolTable) {
        value.validate(st)
    }
}