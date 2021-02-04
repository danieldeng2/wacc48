package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.expr.ExprNode
import analyser.nodes.type.BoolType
import exceptions.SemanticsException

data class IfNode(
    private val proposition: ExprNode,
    private val trueStat: StatNode,
    private val falseStat: StatNode,
) : StatNode {

    override fun validate(st: SymbolTable) {
        val currST = SymbolTable(st)

        proposition.validate(currST)

        if (proposition.type != BoolType)
            throw SemanticsException("If statement proposition must be boolean")

        trueStat.validate(currST)
        falseStat.validate(currST)
    }
}