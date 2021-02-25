package generator.translator

import analyser.SymbolTable
import generator.armInstructions.BLInstr
import generator.armInstructions.Instruction
import generator.armInstructions.LabelInstr
import generator.armInstructions.directives.Ascii
import generator.armInstructions.directives.Word
import generator.translator.print.*

class TranslatorContext {

    var isDeclaring = false

    private var msgCounter = 0
    private var labelCounter = 0
    var stackPtrOffset = 0

    private var stringMap: MutableMap<String, Int> = mutableMapOf()

    private val printList = mutableListOf<PrintSyscall>()

    private val data = mutableListOf<Instruction>().apply {
        add(LabelInstr("data", isSection = true))
    }

    private val text = mutableListOf<Instruction>().apply {
        add(LabelInstr("text", isSection = true))
        add(LabelInstr("global main", isGlobalHeader = true))
    }

    /** Adds the built-in print helper methods to the assembly program
     *  @return jump instruction [BLInstr] to helper method
     */
    fun addPrintFunc(printFunc: PrintSyscall): BLInstr {
        if (printFunc !in printList) {
            printFunc.initFormatters(this)
            printList.add(printFunc)
        }

        return BLInstr(printFunc.label)
    }

    /** Adds the string [msg] into the [data] section of the assembly file
     *  @return integer index of the message
     */
    fun addMessage(msg: String): Int {
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
    ) = text.addAll(instructions)


    fun getAndIncLabelCnt() = labelCounter++

    /** Puts together all the assembly instructions to form
     *  a complete assembly file
     *  @return list of all instructions in the output assembly file
     */
    fun assemble(): List<Instruction> = mutableListOf<Instruction>().apply {
        if (data.size > 1)
            addAll(data)

        text.addAll(printList.flatMap { it.translate() })
        addAll(text)
    }

    fun getOffsetOfLocalVar(id: String, st: SymbolTable) =
        st.getOffsetOfVar(id) + stackPtrOffset
}