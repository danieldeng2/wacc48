package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.assignment.RHSNode
import analyser.nodes.function.ParamNode
import exceptions.SemanticsException
import org.antlr.v4.runtime.ParserRuleContext

data class DeclarationNode(
    private val name: ParamNode,
    private val value: RHSNode,
    override val ctx: ParserRuleContext?
) : StatNode {

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        name.validate(st, funTable)
        value.validate(st, funTable)

        if (value.type != name.type)
            throw SemanticsException("Type mismatch in declaration $name", ctx)
    }
}
