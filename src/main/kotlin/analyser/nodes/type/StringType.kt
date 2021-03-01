package analyser.nodes.type

import analyser.SymbolTable
import analyser.nodes.function.FuncNode
import generator.instructions.Instruction
import generator.translator.TranslatorContext

object StringType : Type {
    override val reserveStackSize: Int = 4

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
    }

    override fun toString(): String {
        return "String"
    }

    override fun translate(ctx: TranslatorContext): List<Instruction> = emptyList()

}