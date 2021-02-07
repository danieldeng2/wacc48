package analyser.nodes.statement

import analyser.SymbolTable
import org.antlr.v4.runtime.ParserRuleContext

data class SeqNode(
    val firstStat: StatNode,
    val secondStat: StatNode,
    override val ctx: ParserRuleContext?,
) : StatNode {
    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        firstStat.validate(st, funTable)
        secondStat.validate(st, funTable)
    }
}