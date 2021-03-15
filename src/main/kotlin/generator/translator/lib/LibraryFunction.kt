package generator.translator.lib

import generator.instructions.Instruction
import generator.translator.TranslatorContext

interface LibraryFunction {
    val label: String

    fun generateArm(): List<Instruction>

    fun generatex86(): List<Instruction>

    fun initIndex(ctx: TranslatorContext)
}