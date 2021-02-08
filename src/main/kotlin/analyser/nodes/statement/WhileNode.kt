package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.expr.ExprNode
import analyser.nodes.type.BoolType
import exceptions.SemanticsException
import org.antlr.v4.runtime.ParserRuleContext

data class WhileNode(
    val proposition: ExprNode,
    val body: StatNode,
    override val ctx: ParserRuleContext?,
) : StatNode {

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        proposition.validate(st, funTable)

        if (proposition.type != BoolType)
            throw SemanticsException("While statement proposition must be boolean", ctx)

        body.validate(SymbolTable(st), funTable)
    }
}

