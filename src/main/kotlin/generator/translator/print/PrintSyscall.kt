package generator.translator.print

import generator.translator.TranslatorContext
import generator.armInstructions.Instruction

interface PrintSyscall {
    val label: String
    fun translate(): List<Instruction>
    fun initIndex(ctx: TranslatorContext)
}