package generator.instructions.directives

import generator.instructions.Instruction

class Ascii(val msg: String) : Instruction {

    private val msgLen = msg.length - msg.filter { it == '\\' }.count()

    override fun tox86() = listOf(
        mutableListOf<String>().apply {
            val temp =
                msg.removeSuffix("\\0").removeSuffix("\\n").replace("\\","")

            add("\tdb \'$temp\'")
            add("0")

            if (msg.removeSuffix("\\0").endsWith("\\n"))
                add("0xA")
        }.joinToString(separator = ", ")
    )

    override fun toArm() = listOf(
        "\t.word $msgLen",
        "\t.ascii \"$msg\""
    ).joinToString(separator = "\n")
}