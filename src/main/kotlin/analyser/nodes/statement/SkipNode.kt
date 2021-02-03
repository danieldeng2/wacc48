package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.ASTNode

object SkipNode : StatNode {

    override fun validate(st: SymbolTable) {
    }

    override fun toString(): String {
        return "Skip"
    }
}
