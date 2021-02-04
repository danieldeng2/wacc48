package analyser.nodes.expr

import analyser.SymbolTable
import analyser.nodes.type.Type
import analyser.nodes.type.VoidType

data class UnOpNode(
    val operator: String,
    val expr: ExprNode,
) : ExprNode {
    override var type: Type = VoidType

    override fun validate(st: SymbolTable) {
        expr.validate(st)

        //TODO: validate type
    }
}