package analyser.nodes.type

import analyser.SymbolTable
import analyser.nodes.function.FuncNode
import generator.instructions.Instruction
import generator.translator.TranslatorContext
import org.antlr.v4.runtime.ParserRuleContext

object BoolType : Type {
    override val reserveStackSize: Int = 1
    override val ctx: ParserRuleContext? = null
    override lateinit var st: SymbolTable
    override lateinit var funTable: MutableMap<String, FuncNode>

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.st = st
        this.funTable = funTable
    }

    override fun toString(): String {
        return "Bool"
    }

    override fun translate(ctx: TranslatorContext): List<Instruction> = emptyList()

}