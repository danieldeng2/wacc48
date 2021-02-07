package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.expr.ExprNode
import analyser.nodes.type.GenericPair
import exceptions.SemanticsException

data class FreeNode(
    private val value: ExprNode,
) : StatNode {

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        value.validate(st, funTable)

        if (value.type !is GenericPair)
            throw SemanticsException(".*", null)
    }
}