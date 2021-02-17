package analyser.nodes.type

import analyser.SymbolTable
import org.antlr.v4.runtime.ParserRuleContext

object VoidType : Type {
    override val ctx: ParserRuleContext? = null
    override lateinit var st: SymbolTable
    override lateinit var funTable: SymbolTable

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        this.st = st
        this.funTable = funTable
    }

    override fun toString(): String {
        return "Void"
    }

    override fun equals(other: Any?): Boolean {
        return other is Type
    }

    override fun hashCode(): Int {
        return 0
    }
}