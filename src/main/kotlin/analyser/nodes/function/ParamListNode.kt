package analyser.nodes.function

import analyser.SymbolTable
import analyser.nodes.ASTNode

class ParamListNode(val params: List<ParamNode>) : ASTNode {
    override fun validate(st: SymbolTable) {
        params.forEach {
            it.validate(st)
        }
    }
}
