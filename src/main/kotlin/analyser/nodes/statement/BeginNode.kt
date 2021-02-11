package analyser.nodes.statement

import analyser.SymbolTable
import org.antlr.v4.runtime.ParserRuleContext

data class BeginNode(
    val stat: StatNode, override val ctx: ParserRuleContext?,
) : StatNode {

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        val currST = SymbolTable(st)
        stat.validate(currST, funTable)
    }
}