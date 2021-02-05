package analyser.nodes.statement

import analyser.SymbolTable
import exceptions.SyntaxException

data class SeqNode(
    val firstStat: StatNode,
    val secondStat: StatNode,
) : StatNode {
    override fun validate(st: SymbolTable) {
        firstStat.validate(st)
        secondStat.validate(st)
    }
}