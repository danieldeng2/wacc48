package analyser.nodes

import analyser.SymbolTable
import org.antlr.v4.runtime.ParserRuleContext

interface ASTNode {

    val ctx: ParserRuleContext?
    val st: SymbolTable
    val funTable: SymbolTable

    fun validate(st: SymbolTable, funTable: SymbolTable)

}
