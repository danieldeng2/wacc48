package generator.translator.print

import generator.translator.TranslatorContext
import generator.instructions.Instruction

interface PrintSyscall {
    val label: String
    fun translate(): List<Instruction>
    fun initIndex(ctx: TranslatorContext)
}