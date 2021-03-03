package generator.translator.lib

import generator.translator.TranslatorContext
import generator.instructions.Instruction

interface LibraryFunction {
    val label: String
    fun translate(): List<Instruction>
    fun initIndex(ctx: TranslatorContext)
}