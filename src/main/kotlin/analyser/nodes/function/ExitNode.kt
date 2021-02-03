package analyser.nodes.function

import analyser.SymbolTable
import analyser.nodes.ASTNode
import analyser.nodes.expr.ExprNode

class ExitNode(value: ExprNode) : ASTNode {

    override fun isValid(st: SymbolTable): Boolean {
        TODO("Not yet implemented")
    }

}
