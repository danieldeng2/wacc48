package generator

import analyser.SymbolTable
import generator.armInstructions.BLInstr
import generator.armInstructions.Instruction
import generator.armInstructions.LabelInstr
import generator.armInstructions.directives.Ascii
import generator.armInstructions.directives.Word
import generator.translator.print.PrintSyscall
import generator.translator.print.PrintInt
import generator.translator.print.PrintLn
import generator.translator.print.PrintStr

/**
 * Each [FuncCode] represents a label and a set of instructions for that label
 * where those set of instructions must only consist of mnemonics
 * i.e. not recursive
 */
typealias FuncCode = List<Instruction>


class TranslatorContext {

    var isDeclaring = false

    private var msgCounter = 0
    private var labelCounter = 0
    var stackPtrOffset = 0

    private var msgMap: MutableMap<PrintSyscall, Int> = mutableMapOf()
    private var stringMap: MutableMap<String, Int> = mutableMapOf()

    private val data = mutableListOf<Instruction>()
    private val text = mutableListOf<FuncCode>()

    /** Adds the built-in print helper methods to the assembly program
     *  @return jump instruction [BLInstr] to helper method
     */
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

    /** Adds the string [msg] into the [data] section of the assembly file
     *  @return integer index of the message
     */
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

    /** Adds a [FuncCode] into the [text] section of the assembly file.
     *  If [labelName] not specified, this is considered a 'helper' method,
     *  which starts with "L" and ends with an incremental integer index
     */
    fun addFunc(
        instructions: List<Instruction>
    ) = text.addAll(listOf(instructions))


    fun getAndIncLabelCnt() = labelCounter++

    /** Puts together all the assembly instructions to form
     *  a complete assembly file
     *  @return list of all instructions in the output assembly file
     */
    fun assemble(): List<Instruction> = mutableListOf<Instruction>().apply {
        if (data.isNotEmpty()) {
            add(LabelInstr("data", isSection = true))
            addAll(data)
        }

        add(LabelInstr("text", isSection = true))
        add(LabelInstr("global main", isGlobalHeader = true))
        text.forEach { addAll(it) }

        msgMap.forEach { addAll(it.key.translate(it.value)) }
    }

    fun getOffsetOfLocalVar(id: String, st: SymbolTable) =
        st.getOffsetOfVar(id) + stackPtrOffset
}

enum class PrintOptions(
    val label: String,
    val printObj: PrintSyscall,
    val stringFormatter: String
) {
    INT("p_print_int", PrintInt, "%d\\0"),
    NEWLINE("p_print_ln", PrintLn, "\\0"),
    STRING("p_print_string", PrintStr, "%.*s\\0")


}

