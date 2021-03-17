package wacc48.generator.translator.lib

import wacc48.generator.instructions.Instruction
import wacc48.generator.translator.TranslatorContext

interface LibraryFunction {
    val label: String

    fun generateArm(): List<Instruction>

    fun generatex86(): List<Instruction>

    fun initIndex(ctx: TranslatorContext)
}