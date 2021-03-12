package generator.translator

import generator.instructions.Instruction
import generator.instructions.Syscall
import generator.instructions.branch.BLInstr
import generator.instructions.directives.Ascii
import generator.instructions.directives.LabelInstr
import generator.translator.lib.LibraryFunction
import tree.SymbolTable

class TranslatorContext {

    /** Counter for the number of messages assigned to the '.data' section
     * of the assembly file. This corresponds to the index when we have
     * '=msg_x' operands. */
    private var msgCounter = 0

    /** Counter for the number of labels assigned. This is used for example
     * as shown: 'L0' 'L1'.*/
    var labelCounter = 0
        get() = field++

    /** Tracks the current offset of the stack pointer relative to its
     * position at the start of the current function in context. */
    var stackPtrOffset = 0

    /** A map which tracks a string in the '.data' section to its corresponding
     * value of [msgCounter]. This ensures that no field in '.data' contains
     * the same string to save memory. */
    private var stringMap: MutableMap<String, Int> = mutableMapOf()

    /** The list of built-in functions that have been called, for example
     * [PrintInt] or [CheckArrayBounds] to make sure they are only printed
     * once to the assembly file. */
    private val usedLibraryFunctions = mutableListOf<LibraryFunction>()

    /** List of instructions in the '.data' section in the assembly file. */
    private val data = mutableListOf<Instruction>().apply {
        add(LabelInstr("data", isSection = true))
    }

    /** List of instructions in the '.tedt' section in the assembly file. */
    val text = mutableListOf<Instruction>().apply {
        add(LabelInstr("text", isSection = true))
        add(LabelInstr("global main", isGlobalHeader = true))
    }

    /** Adds the built-in print helper methods to the assembly program
     *  @return jump instruction [BLInstr] to helper method
     */
    fun addLibraryFunction(builtinFunc: LibraryFunction) {
        if (builtinFunc !in usedLibraryFunctions) {
            builtinFunc.initIndex(this)
            usedLibraryFunctions.add(builtinFunc)
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
            add(Ascii(msg))
        }
        return msgCounter++
    }


    /** Puts together all the assembly instructions to form
     *  a complete assembly file
     *  @return list of all instructions in the output assembly file
     */
    fun assembleArm(): List<Instruction> =
        mutableListOf<Instruction>().apply {
            if (data.size > 1)
                addAll(data)

            text.addAll(usedLibraryFunctions.flatMap { it.generateArm() })
            addAll(text)
        }

    fun assemblex86(): List<Instruction> =
        mutableListOf<Instruction>().apply {
            text.addAll(usedLibraryFunctions.flatMap { it.generatex86() })

            if (data.size > 1)
                addAll(data)

            Syscall.requiredSyscalls.forEach {
                add(LabelInstr("extern $it", isGlobalHeader = true))
            }
            addAll(text)
        }

    /** Calculate the offset of variable with identifier [id], relative
     * to the current position of the stack pointer. */
    fun getOffsetOfVar(id: String, st: SymbolTable) =
        st.getVariablePosition(id) + stackPtrOffset
}