package analyser.nodes.expr

import analyser.SymbolTable
import analyser.nodes.type.CharType
import analyser.nodes.type.Type

data class CharLiteral(val value: Char) : ExprNode {
    override var type: Type = CharType

    override fun validate(st: SymbolTable) {
    }
}