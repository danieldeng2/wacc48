package generator.translator

import analyser.SymbolTable
import generator.armInstructions.BLInstr
import generator.armInstructions.Instruction
import generator.armInstructions.LabelInstr
import generator.armInstructions.directives.Ascii
import generator.armInstructions.directives.Word
import generator.translator.print.*

class TranslatorContext {
    private var msgCounter = 0

    var labelCounter = 0
        get() = field++

    var stackPtrOffset = 0

    private var stringMap: MutableMap<String, Int> = mutableMapOf()

    private val printList = mutableListOf<PrintSyscall>()

    private val data = mutableListOf<Instruction>().apply {
        add(LabelInstr("data", isSection = true))
    }

    val text = mutableListOf<Instruction>().apply {
        add(LabelInstr("text", isSection = true))
        add(LabelInstr("global main", isGlobalHeader = true))
    }

    /** Adds the built-in print helper methods to the assembly program
     *  @return jump instruction [BLInstr] to helper method
     */
    fun addPrintFunc(printFunc: PrintSyscall) {
        if (printFunc !in printList) {
            printFunc.initIndex(this)
            printList.add(printFunc)
        }
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


    /** Puts together all the assembly instructions to form
     *  a complete assembly file
     *  @return list of all instructions in the output assembly file
     */
    fun assemble(): List<Instruction> =
        mutableListOf<Instruction>().apply {
            if (data.size > 1)
                addAll(data)

            text.addAll(printList.flatMap { it.translate() })
            addAll(text)
        }

    fun getOffsetOfLocalVar(id: String, st: SymbolTable) =
        st.getOffsetOfVar(id) + stackPtrOffset
}