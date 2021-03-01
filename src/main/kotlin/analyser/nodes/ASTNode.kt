package analyser.nodes

import analyser.SymbolTable
import analyser.nodes.function.FuncNode
import generator.instructions.Instruction
import generator.translator.TranslatorContext
import org.antlr.v4.runtime.ParserRuleContext

interface ASTNode {

    val ctx: ParserRuleContext?
    val st: SymbolTable

    fun validate(st: SymbolTable, funTable: MutableMap<String, FuncNode>)

    fun translate(ctx: TranslatorContext): List<Instruction>
}
