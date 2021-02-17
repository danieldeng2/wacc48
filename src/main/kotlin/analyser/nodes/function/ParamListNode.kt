package analyser.nodes.function

import analyser.SymbolTable
import analyser.nodes.ASTNode
import org.antlr.v4.runtime.ParserRuleContext

data class ParamListNode(
    val params: List<ParamNode>,
    override val ctx: ParserRuleContext?
) : ASTNode {
    override lateinit var st: SymbolTable
    override lateinit var funTable: SymbolTable


    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        this.st = st
        this.funTable = funTable
        params.forEach {
            it.validate(st, funTable)
        }
    }
}
