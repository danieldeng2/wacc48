package datastructures.nodes.statement

import datastructures.SymbolTable
import datastructures.nodes.function.FuncNode
import generator.instructions.Instruction
import generator.translator.TranslatorContext

object SkipNode : StatNode {

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
    }

    override fun toString(): String {
        return "Skip"
    }

    override fun translate(ctx: TranslatorContext): List<Instruction> =
        emptyList()
}
