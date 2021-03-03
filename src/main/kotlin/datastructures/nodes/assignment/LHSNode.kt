package datastructures.nodes.assignment

import datastructures.nodes.ASTNode
import datastructures.type.Typable

enum class AccessMode {
    ASSIGN, READ, ADDRESS
}

interface LHSNode : ASTNode, Typable {
    var mode: AccessMode
}
