package analyser.nodes

import analyser.SymbolTable
import generator.armInstructions.Instruction
import generator.translator.TranslatorContext
import org.antlr.v4.runtime.ParserRuleContext

interface ASTNode {

    val ctx: ParserRuleContext?
    val st: SymbolTable
    val funTable: SymbolTable

    fun validate(st: SymbolTable, funTable: SymbolTable)

    fun translate(ctx: TranslatorContext): List<Instruction> = TODO()
}
