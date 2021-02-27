package analyser.nodes.assignment

import analyser.nodes.ASTNode
import analyser.nodes.type.Typable

enum class AccessMode {
    ASSIGN, READ, ADDRESS
}

interface LHSNode : ASTNode, Typable {
    var mode: AccessMode
}

