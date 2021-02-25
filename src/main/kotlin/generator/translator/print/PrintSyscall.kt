package generator.translator.print

import generator.translator.TranslatorContext
import generator.armInstructions.Instruction

interface PrintSyscall {
    var label: String
    fun translate(): List<Instruction>
    fun initFormatters(ctx: TranslatorContext)
}