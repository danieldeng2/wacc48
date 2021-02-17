package analyser.nodes.expr

import analyser.SymbolTable
import analyser.nodes.type.StringType
import analyser.nodes.type.Type
import org.antlr.v4.runtime.ParserRuleContext


data class StringLiteral(
    val value: String,
    override val ctx: ParserRuleContext?
) : ExprNode {
    override lateinit var st: SymbolTable
    override lateinit var funTable: SymbolTable

    override var type: Type = StringType

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        this.st = st
        this.funTable = funTable
    }
}