package analyser.nodes.type

import analyser.SymbolTable
import analyser.nodes.function.FuncNode
import generator.instructions.Instruction
import generator.translator.TranslatorContext
import org.antlr.v4.runtime.ParserRuleContext

object EmptyPair : GenericPair {
    override val ctx: ParserRuleContext? = null
    override lateinit var st: SymbolTable

    override val reserveStackSize: Int = 4


    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.st = st
    }

    override fun equals(other: Any?): Boolean {
        return other is GenericPair
    }

    override fun hashCode(): Int {
        return 1
    }

    override fun toString(): String {
        return "Pair"
    }

    override fun translate(ctx: TranslatorContext): List<Instruction> = emptyList()
}

