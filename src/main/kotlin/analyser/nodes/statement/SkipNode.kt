package analyser.nodes.statement

import analyser.SymbolTable

object SkipNode : StatNode {

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
    }

    override fun toString(): String {
        return "Skip"
    }
}
