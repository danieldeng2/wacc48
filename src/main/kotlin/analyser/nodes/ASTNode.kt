package analyser.nodes

import analyser.SymbolTable

interface ASTNode {

    fun validate(st: SymbolTable)

}
