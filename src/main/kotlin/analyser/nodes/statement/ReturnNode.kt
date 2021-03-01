package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.expr.ExprNode
import analyser.nodes.function.FuncNode
import generator.translator.TranslatorContext
import org.antlr.v4.runtime.ParserRuleContext

data class ReturnNode(
    val value: ExprNode,
    val ctx: ParserRuleContext?,
) : StatNode {


    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {

        value.validate(st, funTable)
    }

    override fun translate(ctx: TranslatorContext) = value.translate(ctx)
}