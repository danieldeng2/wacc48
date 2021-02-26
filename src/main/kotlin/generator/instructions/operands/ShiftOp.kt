package generator.instructions.operands

class ShiftOp(val reg: Register, val num: NumOp) :
    LoadableOp {

    override fun toString() = "$reg, ASR $num"
}
