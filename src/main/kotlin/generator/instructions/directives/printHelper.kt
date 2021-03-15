package generator.instructions.directives

import generator.instructions.Instruction

class Ascii(val msg: String) : Instruction {

    private val msgLen = msg.length - msg.filter { it == '\\' }.count()

    private val escapeCharMap = mapOf(
        "\\t" to "0x09",
        "\\n" to "0x0A",
        "\\r" to "0x0D",
        "\\b" to "0x08",
        "\\f" to "0x0C",
        "\"" to "\'\"\'"
    )

    override fun tox86(): List<String> {
        var stripNull = msg.removeSuffix("\\0")
        escapeCharMap.forEach { (chr, rep) ->
            stripNull = stripNull.replace(chr, "\\$rep\\")
        }

        val partitions = stripNull
            .split("\\")
            .joinToString(separator = ",") {
                if (it.startsWith("0x") || it == "\'\"\'")
                    it
                else
                    "\"$it\""
            }

        return listOf("\tdb $partitions, 0")
    }

    override fun toArm() = listOf(
        "\t.word $msgLen",
        "\t.ascii \"$msg\""
    ).joinToString(separator = "\n")
}