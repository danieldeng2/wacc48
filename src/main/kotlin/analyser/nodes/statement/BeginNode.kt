package analyser.nodes.statement

import analyser.SymbolTable

data class BeginNode(
    val stat: StatNode,
) : StatNode {

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        val currST = SymbolTable(st)
        stat.validate(currST, funTable)
    }
}