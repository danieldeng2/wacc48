package analyser.nodes.function

import analyser.SymbolTable
import analyser.nodes.ASTNode

data class ParamListNode(val params: List<ParamNode>) : ASTNode {
    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        params.forEach {
            it.validate(st, funTable)
        }
    }
}
