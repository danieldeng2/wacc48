package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.function.FuncNode
import generator.instructions.Instruction
import generator.translator.TranslatorContext
import org.antlr.v4.runtime.ParserRuleContext

object SkipNode : StatNode {
    override lateinit var st: SymbolTable


    override val ctx: ParserRuleContext? = null

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.st = st
    }

    override fun toString(): String {
        return "Skip"
    }

    override fun translate(ctx: TranslatorContext): List<Instruction> =
        emptyList()
}
