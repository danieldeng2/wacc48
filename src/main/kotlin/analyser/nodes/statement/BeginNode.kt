package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.function.FuncNode
import generator.instructions.Instruction
import generator.translator.TranslatorContext
import generator.translator.newScope
import org.antlr.v4.runtime.ParserRuleContext

data class BeginNode(
    val stat: StatNode,
    val ctx: ParserRuleContext?,
) : StatNode {
    private lateinit var currST: SymbolTable

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.currST = SymbolTable(st)
        stat.validate(currST, funTable)
    }

    override fun translate(ctx: TranslatorContext) =
        mutableListOf<Instruction>().apply {
            newScope(currST) {
                addAll(stat.translate(ctx))
            }
        }
}