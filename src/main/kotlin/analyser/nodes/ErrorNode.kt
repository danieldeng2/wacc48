package analyser.nodes

import analyser.SymbolTable

class ErrorNode : ASTNode {

    override fun isValid(st: SymbolTable): Boolean = false
}