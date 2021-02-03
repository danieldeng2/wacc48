package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.expr.ExprNode
import analyser.nodes.type.BoolType
import exceptions.SemanticsException

data class WhileNode(
    private val proposition: ExprNode,
    private val body: StatNode,
) : StatNode {

    override fun validate(st: SymbolTable) {
        val currST = SymbolTable(st)

        proposition.validate(currST)

        if (proposition.type != BoolType)
            throw SemanticsException("While statement proposition must be boolean")

        body.validate(currST)
    }
}

