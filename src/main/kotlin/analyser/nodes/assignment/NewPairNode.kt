package analyser.nodes.assignment

import analyser.SymbolTable
import analyser.nodes.expr.ExprNode
import analyser.nodes.type.PairType
import analyser.nodes.type.Type
import analyser.nodes.type.VoidType
import org.antlr.v4.runtime.ParserRuleContext

data class NewPairNode(
    val firstElem: ExprNode,
    val secondElem: ExprNode,
    override val ctx: ParserRuleContext?
) : RHSNode {
    override var type: Type = VoidType

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        firstElem.validate(st, funTable)
        secondElem.validate(st, funTable)

        type = PairType(firstElem.type, secondElem.type, ctx)
    }

}