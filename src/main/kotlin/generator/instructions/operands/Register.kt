package generator.instructions.operands

enum class Register(val repr: String) : LoadableOp, WritableOp {
    PC("pc"),
    LR("lr"),
    SP("sp"),
    R0("r0"),
    R1("r1"),
    R2("r2"),
    R3("r3"),
    R4("r4");

    override fun toString() = repr
}