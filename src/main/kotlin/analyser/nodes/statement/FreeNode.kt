package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.expr.ExprNode
import analyser.nodes.type.GenericPair
import exceptions.SemanticsException

data class FreeNode(
    private val value: ExprNode,
) : StatNode {

    override fun validate(st: SymbolTable) {
        value.validate(st)

        if (value.type !is GenericPair)
            throw SemanticsException("Cannot free ${value.type}")
    }
}