package generator.instructions.operands

class ShiftOp(val reg: Register, val shiftType: ShiftType, val num: NumOp) :
    ImmOp {

    override fun toString() = "$reg, $shiftType $num"
}

enum class ShiftType {
    ASR,
    LSL
}