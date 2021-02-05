package analyser.nodes.expr

import analyser.SymbolTable
import analyser.nodes.type.BoolType
import analyser.nodes.type.Type

data class BoolLiteral(val value: Boolean) : ExprNode {
    override var type: Type = BoolType

    override fun validate(st: SymbolTable) {
    }
}