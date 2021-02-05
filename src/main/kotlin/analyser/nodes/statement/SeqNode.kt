package analyser.nodes.statement

import analyser.SymbolTable

data class SeqNode(
    val firstStat: StatNode,
    val secondStat: StatNode,
) : StatNode {
    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        firstStat.validate(st, funTable)
        secondStat.validate(st, funTable)
    }
}