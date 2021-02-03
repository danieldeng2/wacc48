package analyser.nodes.function

import analyser.SymbolTable
import analyser.nodes.ASTNode
import analyser.nodes.expr.ExprNode

class ExitNode(value: ExprNode) : ASTNode {

    override fun validate(st: SymbolTable) {
        TODO("Not yet implemented")
    }

}
