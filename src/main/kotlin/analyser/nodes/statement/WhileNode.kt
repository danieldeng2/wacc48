package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.expr.ExprNode
import analyser.nodes.type.BoolType
import exceptions.SemanticsException

data class WhileNode(
    val proposition: ExprNode,
    val body: StatNode,
) : StatNode {

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        proposition.validate(st, funTable)

        if (proposition.type != BoolType)
            throw SemanticsException(".*", null)

        body.validate(SymbolTable(st), funTable)
    }
}

