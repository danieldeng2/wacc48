package analyser.nodes.statement

import analyser.SymbolTable
import generator.TranslatorContext
import generator.armInstructions.Instruction
import org.antlr.v4.runtime.ParserRuleContext

object SkipNode : StatNode {
    override lateinit var st: SymbolTable
    override lateinit var funTable: SymbolTable

    override val ctx: ParserRuleContext? = null

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        this.st = st
        this.funTable = funTable
    }

    override fun toString(): String {
        return "Skip"
    }

    override fun translate(ctx: TranslatorContext): List<Instruction> =
        emptyList()
}
