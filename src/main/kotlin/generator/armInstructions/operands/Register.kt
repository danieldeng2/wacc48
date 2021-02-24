package generator.armInstructions.operands

enum class Register(val repr: String) : LoadableOp, WritableOp {
    PC("pc"),
    LR("lr"),
    SP("sp"),
    R0("r0"),
    R1("r1");

    override fun toString() = repr
}