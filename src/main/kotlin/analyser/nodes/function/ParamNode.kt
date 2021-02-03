package analyser.nodes.function

import analyser.SymbolTable
import analyser.nodes.type.Type
import analyser.nodes.ASTNode

class ParamNode(val type: Type, val text: String) : ASTNode {
    override fun isValid(st: SymbolTable): Boolean {
        TODO("Not yet implemented")
    }
}