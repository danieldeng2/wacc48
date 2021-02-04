package analyser.nodes.expr

import analyser.SymbolTable
import analyser.nodes.type.Type
import analyser.nodes.type.VoidType

data class BinOpNode(
    val operator: String,
    val firstExpr: ExprNode,
    val secondExpr: ExprNode
) : ExprNode {
    override var type: Type = VoidType

    override fun validate(st: SymbolTable) {
        firstExpr.validate(st)
        secondExpr.validate(st)

        //TODO: validate type
    }
}