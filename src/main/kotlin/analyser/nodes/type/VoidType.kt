package analyser.nodes.type

import analyser.SymbolTable
import generator.instructions.Instruction
import generator.translator.TranslatorContext
import org.antlr.v4.runtime.ParserRuleContext

object VoidType : Type {
    override val ctx: ParserRuleContext? = null
    override lateinit var st: SymbolTable
    override lateinit var funTable: SymbolTable

    override val reserveStackSize: Int = 0

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        this.st = st
        this.funTable = funTable
    }

    override fun toString(): String {
        return "Void"
    }

    override fun equals(other: Any?): Boolean {
        return other is Type
    }

    override fun hashCode(): Int {
        return 0
    }

    override fun translate(ctx: TranslatorContext): List<Instruction> = emptyList()
}