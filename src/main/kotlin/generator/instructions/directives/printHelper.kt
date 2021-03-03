package generator.instructions.directives

import generator.instructions.Instruction

class Word(val numBytes: Int) : Instruction {
    override fun toString() = "\t.word $numBytes"
}

class Ascii(val msg: String) : Instruction {
    override fun toString() = "\t.ascii \"$msg\""
}