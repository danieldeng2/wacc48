package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.expr.ExprNode
import org.antlr.v4.runtime.ParserRuleContext

data class PrintNode(
    private val value: ExprNode,
    private val returnAfterPrint: Boolean = false,
    override val ctx: ParserRuleContext?,
) : StatNode {

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        value.validate(st, funTable)
    }
}