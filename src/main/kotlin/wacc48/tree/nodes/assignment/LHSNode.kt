package wacc48.tree.nodes.assignment

import wacc48.tree.nodes.ASTNode
import wacc48.tree.type.Typable

enum class AccessMode {
    ASSIGN, READ, ADDRESS
}

interface LHSNode : ASTNode, Typable {
    var mode: AccessMode
}
