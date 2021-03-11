package generator.instructions.directives

import generator.instructions.Instruction

class Ascii(val msg: String) : Instruction {

    private val msgLen = msg.length - msg.filter { it == '\\' }.count()

    override fun tox86() = listOf(
        if (msg.endsWith("\\0")) "\tdb \"${msg.removeSuffix("\\0")}\", 0"
        else "\tdb \"$msg\""
    )

    override fun toArm() = listOf(
        "\t.word $msgLen",
        "\t.ascii \"$msg\""
    ).joinToString(separator = "\n")
}