package analyser.nodes.expr

import analyser.SymbolTable
import analyser.nodes.type.IntType
import analyser.nodes.type.Type
import exceptions.SemanticsException

data class IntLiteral(val value: Int) : ExprNode {
    override var type: Type = IntType

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        if (value > IntType.max || value < IntType.min)
            throw SemanticsException("IntLiteral $value is out of range")
    }
}