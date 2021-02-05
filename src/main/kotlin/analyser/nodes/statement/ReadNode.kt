package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.expr.ExprNode
import analyser.nodes.type.CharType
import analyser.nodes.type.IntType
import analyser.nodes.type.StringType
import analyser.nodes.type.Type
import exceptions.SemanticsException

data class ReadNode(
    private val value: ExprNode,
) : StatNode {
    private val expectedExprTypes: List<Type> = listOf(IntType, StringType, CharType)

    override fun validate(st: SymbolTable) {
        value.validate(st)
        if (value.type !in expectedExprTypes)
            throw SemanticsException("Cannot read from type ${value.type}")
    }
}