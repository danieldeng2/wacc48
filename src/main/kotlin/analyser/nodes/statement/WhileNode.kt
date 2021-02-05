package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.expr.ExprNode
import analyser.nodes.type.BoolType
import exceptions.SemanticsException

data class WhileNode(
    val proposition: ExprNode,
    val body: StatNode,
) : StatNode {

    override fun validate(st: SymbolTable) {
        proposition.validate(st)

        if (proposition.type != BoolType)
            throw SemanticsException("While statement proposition must be boolean")

        body.validate(SymbolTable(st))
    }
}

