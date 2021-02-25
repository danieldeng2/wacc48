package generator.armInstructions.directives

import generator.armInstructions.Instruction

class Word(val numBytes: Int) : Instruction {
    override fun toString() = "\t.word $numBytes"
}

class Ascii(val msg: String) : Instruction {
    override fun toString() = "\t.ascii \"$msg\""
}