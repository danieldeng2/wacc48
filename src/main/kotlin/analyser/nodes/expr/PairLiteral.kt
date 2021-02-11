package analyser.nodes.expr

import analyser.SymbolTable
import analyser.nodes.type.EmptyPair
import analyser.nodes.type.Type
import org.antlr.v4.runtime.ParserRuleContext

object PairLiteral : ExprNode {
    override var type: Type = EmptyPair
    override val ctx: ParserRuleContext?
        get() = null

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
    }

    override fun toString(): String {
        return "Null"
    }
}
