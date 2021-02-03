package analyser.nodes

import analyser.SymbolTable

interface ASTNode {

    fun isValid(st: SymbolTable): Boolean

}
