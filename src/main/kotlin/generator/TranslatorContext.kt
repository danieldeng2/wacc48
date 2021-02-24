package generator

import generator.armInstructions.Instruction
import generator.armInstructions.LabelInstr
import generator.armInstructions.directives.Ascii
import generator.armInstructions.directives.Word
import generator.print.PrintSyscall
import generator.print.PrintInt
import generator.print.PrintLn

class TranslatorContext {

    var isDeclaring = false

    private var msgCounter = 0
    private var msgMap: MutableMap<PrintSyscall, Int> = mutableMapOf()
    val data = mutableListOf<Instruction>(LabelInstr("data", isSection = true))

    fun addPrintInt() {
        if (msgMap.containsKey(PrintInt)) return
        msgMap[PrintInt] = msgCounter

        data.apply {
            add(LabelInstr("msg_$msgCounter"))
            add(Word(3))
            add(Ascii("%d\\0"))
        }
        msgCounter++
    }

    fun addPrintLn() {
        if (msgMap.containsKey(PrintLn)) return
        msgMap[PrintLn] = msgCounter

        data.apply {
            add(LabelInstr("msg_$msgCounter"))
            add(Word(1))
            add(Ascii("\\0"))
        }
        msgCounter++
    }

    fun translateSyscall(): List<Instruction> {
        val instructions = mutableListOf<Instruction>()

        msgMap.forEach { instructions.addAll(it.key.translate(it.value)) }
        return instructions
    }
}

