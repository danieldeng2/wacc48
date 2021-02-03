package analyser.nodes.function

import analyser.SymbolTable
import analyser.nodes.ASTNode

class ParamListNode(val params: List<ParamNode>) : ASTNode {
    override fun isValid(st: SymbolTable): Boolean {
        TODO("Not yet implemented")
    }

}
