package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.expr.ExprNode
import analyser.nodes.type.IntType
import exceptions.SemanticsException

data class ExitNode(private val value: ExprNode) : StatNode {

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        value.validate(st, funTable)
        if (value.type != IntType)
            throw SemanticsException(".*", null)
    }
}
