package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.assignment.LHSNode
import analyser.nodes.type.CharType
import analyser.nodes.type.IntType
import analyser.nodes.type.StringType
import analyser.nodes.type.Type
import exceptions.SemanticsException
import org.antlr.v4.runtime.ParserRuleContext

data class ReadNode(
    private val value: LHSNode,
    override val ctx: ParserRuleContext?,
) : StatNode {
    private val expectedExprTypes: List<Type> = listOf(IntType, StringType, CharType)

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        value.validate(st, funTable)
        if (value.type !in expectedExprTypes)
            throw SemanticsException("Cannot read from type ${value.type}", ctx)
    }
}