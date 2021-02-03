package analyser.nodes.statement

import analyser.SymbolTable

data class SeqNode(
    private val firstStat: StatNode,
    private val secondStat: StatNode,
) : StatNode {
    override fun validate(st: SymbolTable) {
        firstStat.validate(st)
        secondStat.validate(st)
    }
}