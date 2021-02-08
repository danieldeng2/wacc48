package analyser.nodes.type

import analyser.SymbolTable
import org.antlr.v4.runtime.ParserRuleContext

object BoolType : Type {
    override val ctx: ParserRuleContext? = null

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
    }

    override fun toString(): String {
        return "Bool"
    }
}