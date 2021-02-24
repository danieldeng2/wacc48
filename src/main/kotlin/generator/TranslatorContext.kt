package generator

import generator.armInstructions.BLInstr
import generator.armInstructions.Instruction
import generator.armInstructions.LabelInstr
import generator.armInstructions.directives.Ascii
import generator.armInstructions.directives.Word
import generator.print.PrintSyscall
import generator.print.PrintInt
import generator.print.PrintLn
import generator.print.PrintStr

class TranslatorContext {

    var isDeclaring = false

    private var msgCounter = 0

    private var msgMap: MutableMap<PrintSyscall, Int> = mutableMapOf()
    private var stringMap: MutableMap<String, Int> = mutableMapOf()

    val data = mutableListOf<Instruction>(LabelInstr("data", isSection = true))

    fun addPrintFunc(printOptions: PrintOptions): BLInstr {
        if (msgMap.containsKey(printOptions.printObj))
            return BLInstr(printOptions.label)

        msgMap[printOptions.printObj] = msgCounter
        data.apply {
            add(LabelInstr("msg_$msgCounter"))
            add(Word(printOptions.stringFormatter.length - 1))
            add(Ascii(printOptions.stringFormatter))
        }
        msgCounter++
        return BLInstr(printOptions.label)

    }

    fun translateSyscall(): List<Instruction> {
        val instructions = mutableListOf<Instruction>()

        msgMap.forEach { instructions.addAll(it.key.translate(it.value)) }
        return instructions
    }

    fun addStringToPrint(msg: String): Int {
        if (stringMap.containsKey(msg)) return stringMap[msg]!!

        stringMap[msg] = msgCounter
        data.apply {
            add(LabelInstr("msg_$msgCounter"))
            add(Word(msg.length))
            add(Ascii(msg))
        }
        return msgCounter++
    }
}

enum class PrintOptions(
    val label: String,
    val printObj: PrintSyscall,
    val stringFormatter: String
) {
    INT("p_print_int", PrintInt, "%d\\0"),
    NEWLINE("p_print_ln", PrintLn, "\\0"),
    STRING("p_print_string", PrintStr, "%.s*\\0")


}

