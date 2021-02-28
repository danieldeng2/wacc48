package analyser.nodes.type

import analyser.SymbolTable
import analyser.nodes.function.FuncNode
import generator.instructions.Instruction
import generator.translator.TranslatorContext
import org.antlr.v4.runtime.ParserRuleContext


data class PairType(
    var firstType: Type,
    var secondType: Type,
    override val ctx: ParserRuleContext?
) : GenericPair {

    override lateinit var st: SymbolTable
    override lateinit var funTable: MutableMap<String, FuncNode>
    override val reserveStackSize: Int = 4


    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.st = st
        this.funTable = funTable
    }

    override fun equals(other: Any?): Boolean {
        return other is GenericPair
    }

    override fun hashCode(): Int {
        return 1
    }

    override fun translate(ctx: TranslatorContext): List<Instruction> = emptyList()

}