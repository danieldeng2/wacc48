package analyser.nodes.expr

import analyser.SymbolTable
import analyser.nodes.type.CharType
import analyser.nodes.type.Type
import org.antlr.v4.runtime.ParserRuleContext

data class CharLiteral(val value: Char, override val ctx: ParserRuleContext?) : ExprNode {
    override var type: Type = CharType
    override lateinit var st: SymbolTable
    override lateinit var funTable: SymbolTable

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        this.st = st
        this.funTable = funTable
    }
}