package generator.instructions.operands

class ShiftOp(val reg: Register, val shiftType: ShiftType, val num: NumOp) :
    ImmOp {


    override fun toArm() = "${reg.toArm()}, $shiftType ${num.toArm()}"
}

enum class ShiftType(val x86repr: String) {
    ASR("sar"),
    LSL("shl")
}