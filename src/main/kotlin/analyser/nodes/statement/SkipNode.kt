package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.ASTNode

class SkipNode : ASTNode {

    override fun isValid(st: SymbolTable): Boolean {
        return true
    }

}
