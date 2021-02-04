package analyser.nodes.statement

import analyser.SymbolTable
import exceptions.SyntaxException

data class SeqNode(
    private val firstStat: StatNode,
    private val secondStat: StatNode,
) : StatNode {
    override fun validate(st: SymbolTable) {
        firstStat.validate(st)
        secondStat.validate(st)

        if (firstStat is ReturnNode)
            throw SyntaxException(
                "Function did not end with a return statement"
            )
    }
}