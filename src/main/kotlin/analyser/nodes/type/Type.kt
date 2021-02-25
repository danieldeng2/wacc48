package analyser.nodes.type

import analyser.nodes.ASTNode

interface Type : ASTNode {
    val reserveStackSize: Int
}

interface GenericPair : Type

interface Typable {
    var type: Type
}