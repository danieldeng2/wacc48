package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.expr.ExprNode
import analyser.nodes.type.BoolType
import exceptions.SemanticsException

data class IfNode(
    val proposition: ExprNode,
    val trueStat: StatNode,
    val falseStat: StatNode,
) : StatNode {

    override fun validate(st: SymbolTable, funTable: SymbolTable) {

        if (proposition.type != BoolType)
            throw SemanticsException("If statement proposition must be boolean")

        trueStat.validate(SymbolTable(st), funTable)
        falseStat.validate(SymbolTable(st), funTable)
    }
}