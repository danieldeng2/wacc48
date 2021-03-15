package wacc48.generator.instructions.operands

interface ImmOp : LoadableOp

class NumOp(val value: Int, val isLoad: Boolean = false) : ImmOp {

    override fun tox86() = "$value"

    override fun toArm() = if (isLoad) {
        "=$value"
    } else {
        "#${value}"
    }
}

class CharOp(val value: Char) : ImmOp {
    override fun toArm() = when (value) {
        '\u0000' -> "#0"
        else -> "#\'$value\'"
    }

    override fun tox86() = when (value) {
        '\u0000' -> "0"
        else -> "${value.toInt()}"
    }
}

class LabelOp(val index: Int) : ImmOp {

    override fun tox86() = "msg_$index"

    override fun toArm() = "=msg_$index"

}

