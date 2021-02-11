package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.expr.ExprNode
import analyser.nodes.type.IntType
import exceptions.SemanticsException
import org.antlr.v4.runtime.ParserRuleContext

data class ExitNode(
    private val value: ExprNode,
    override val ctx: ParserRuleContext?
) : StatNode {

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        value.validate(st, funTable)
        if (value.type != IntType)
            throw SemanticsException("Exit must take integer as input, got ${value.type} instead", ctx)
    }
}
