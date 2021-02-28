package analyser.nodes.type

import analyser.SymbolTable
import generator.instructions.Instruction
import generator.translator.TranslatorContext
import org.antlr.v4.runtime.ParserRuleContext

object BoolType : Type {
    override val reserveStackSize: Int = 1
    override val ctx: ParserRuleContext? = null
    override lateinit var st: SymbolTable
    override lateinit var funTable: SymbolTable

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        this.st = st
        this.funTable = funTable
    }

    override fun toString(): String {
        return "Bool"
    }

    override fun translate(ctx: TranslatorContext): List<Instruction> = emptyList()

}