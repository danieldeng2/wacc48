package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.function.FuncNode
import generator.instructions.Instruction
import generator.translator.TranslatorContext
import generator.translator.newScope
import org.antlr.v4.runtime.ParserRuleContext

data class BeginNode(
    val stat: StatNode,
    override val ctx: ParserRuleContext?,
) : StatNode {
    override lateinit var st: SymbolTable

    private lateinit var currST: SymbolTable

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.st = st
        this.currST = SymbolTable(st)
        stat.validate(currST, funTable)
    }

    override fun translate(ctx: TranslatorContext) =
        mutableListOf<Instruction>().apply {

            if (currST.totalVarSize == 0)
                addAll(stat.translate(ctx))
            else
                newScope(currST) {
                    addAll(stat.translate(ctx))
                }

        }
}