package generator.instructions.operands

enum class Register(val repr: String, private val x86repr: String) :
    LoadableOp {
    PC("pc", ""),
    LR("lr", ""),
    SP("sp", "esp"),
    R0("r0", "eax"),
    R1("r1", "edi"),
    R2("r2", "esi"),
    R3("r3", "edx"),
    R4("r4", "ecx");

    override fun toArm() = repr

    override fun tox86() = x86repr
}