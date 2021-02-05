package analyser.nodes.expr

import analyser.SymbolTable
import analyser.nodes.type.StringType
import analyser.nodes.type.Type


data class StringLiteral(val value: String) : ExprNode {
    override var type: Type = StringType

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
    }
}