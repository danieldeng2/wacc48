package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.assignment.LHSNode
import analyser.nodes.type.CharType
import analyser.nodes.type.IntType
import analyser.nodes.type.StringType
import analyser.nodes.type.Type
import exceptions.SemanticsException

data class ReadNode(
    private val value: LHSNode,
) : StatNode {
    private val expectedExprTypes: List<Type> = listOf(IntType, StringType, CharType)

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        value.validate(st, funTable)
        if (value.type !in expectedExprTypes)
            throw SemanticsException(".*", null)
    }
}