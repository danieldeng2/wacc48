package generator.instructions.directives

import generator.instructions.Instruction

class Word(val numBytes: Int) : Instruction {
    override fun toArm() = "\t.word $numBytes"
}

class Ascii(val msg: String) : Instruction {
    override fun tox86() = listOf("\t.ascii \"$msg\"")

    override fun toArm() = "\t.ascii \"$msg\""
}