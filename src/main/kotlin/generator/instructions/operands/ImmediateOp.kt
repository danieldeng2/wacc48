package generator.instructions.operands

interface ImmOp : LoadableOp

class BoolOp(val value: Boolean) : ImmOp {
    override fun toString() = when (value) {
        true -> "1"
        false -> "0"
    }
}

class NumOp(val value: Int, val isLoad: Boolean = false) : ImmOp {
    override fun toString() = if (isLoad) {
        "=$value"
    } else {
        "#${value}"
    }
}

class CharOp(val value: Char) : ImmOp {
    override fun toString() = when (value) {
        '\u0000' -> "#0"
        else -> "#\'$value\'"
    }
}

class LabelOp(val index: Int) : ImmOp {
    override fun toString() = "=msg_$index"

}