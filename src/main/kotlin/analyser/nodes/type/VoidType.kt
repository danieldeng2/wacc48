package analyser.nodes.type

import analyser.SymbolTable
import analyser.nodes.function.FuncNode
import generator.instructions.Instruction
import generator.translator.TranslatorContext

object VoidType : Type {
    override val reserveStackSize: Int = 0

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
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