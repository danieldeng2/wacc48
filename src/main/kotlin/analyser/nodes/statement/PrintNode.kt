package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.expr.ExprNode

data class PrintNode(
    private val value: ExprNode,
    private val returnAfterPrint: Boolean = false,
) : StatNode {

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        value.validate(st, funTable)
    }
}