package analyser.nodes.expr

import analyser.SymbolTable
import analyser.nodes.type.EmptyPair
import analyser.nodes.type.Type

object PairLiteral : ExprNode {
    override var type: Type = EmptyPair

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
    }

    override fun toString(): String {
        return "Null"
    }
}
