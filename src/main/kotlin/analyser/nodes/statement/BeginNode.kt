package analyser.nodes.statement

import analyser.SymbolTable
import org.antlr.v4.runtime.ParserRuleContext

data class BeginNode(
    val stat: StatNode,
    override val ctx: ParserRuleContext?,
) : StatNode {
    override lateinit var st: SymbolTable
    override lateinit var funTable: SymbolTable

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        this.st = st
        this.funTable = funTable
        val currST = SymbolTable(st)
        stat.validate(currST, funTable)
    }
}