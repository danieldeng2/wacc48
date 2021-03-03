package tree.nodes.assignment

import tree.nodes.ASTNode
import tree.type.Typable

enum class AccessMode {
    ASSIGN, READ, ADDRESS
}

interface LHSNode : ASTNode, Typable {
    var mode: AccessMode
}
