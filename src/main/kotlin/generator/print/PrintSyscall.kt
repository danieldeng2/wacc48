package generator.print

import generator.armInstructions.Instruction

interface PrintSyscall {
    fun translate(msgIndex: Int): List<Instruction>
}