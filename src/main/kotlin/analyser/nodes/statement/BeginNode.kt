package analyser.nodes.statement

import analyser.SymbolTable

data class BeginNode(
    private val stat: StatNode,
) : StatNode {

    override fun validate(st: SymbolTable) {
        val currST = SymbolTable(st)
        stat.validate(currST)
    }
}