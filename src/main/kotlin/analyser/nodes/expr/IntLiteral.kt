package analyser.nodes.expr

import analyser.SymbolTable
import analyser.nodes.type.IntType
import exceptions.SemanticsException

data class IntLiteral(val value: Int) : ExprNode {
    override val type = IntType

    override fun validate(st: SymbolTable) {
        if (value > IntType.max || value < IntType.min)
            throw SemanticsException("IntLiteral $value is out of range")
    }
}