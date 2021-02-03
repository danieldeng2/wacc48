package analyser.nodes.expr

import analyser.SymbolTable
import analyser.nodes.type.IntType

class IntLiteral : ExprNode {
    override val type = IntType

    override fun isValid(st: SymbolTable) = true
}