package analyser.nodes.statement

import analyser.SymbolTable
import org.antlr.v4.runtime.ParserRuleContext

object SkipNode : StatNode {
    override val ctx: ParserRuleContext?
        get() = null

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
    }

    override fun toString(): String {
        return "Skip"
    }
}
