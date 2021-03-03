package generator.translator.lib

import generator.instructions.Instruction
import generator.translator.TranslatorContext

interface LibraryFunction {
    val label: String
    fun translate(): List<Instruction>
    fun initIndex(ctx: TranslatorContext)
}