package analyser.nodes.expr

import analyser.SymbolTable
import analyser.nodes.type.BoolType
import analyser.nodes.type.Type
import org.antlr.v4.runtime.ParserRuleContext

data class BoolLiteral(
    val value: Boolean,
    override val ctx: ParserRuleContext?
) : ExprNode {
    override var type: Type = BoolType

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
    }
}