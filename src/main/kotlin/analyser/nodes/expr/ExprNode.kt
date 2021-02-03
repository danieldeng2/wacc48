package analyser.nodes.expr

import analyser.nodes.ASTNode
import analyser.nodes.type.Type

interface ExprNode : ASTNode {
    val type: Type;
}
