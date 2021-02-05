package analyser.nodes.type

import analyser.nodes.ASTNode

interface Type : ASTNode

interface GenericPair : Type

interface Typable {
    var type: Type
}