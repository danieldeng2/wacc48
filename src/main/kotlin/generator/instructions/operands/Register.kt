package generator.instructions.operands

abstract class Register : LoadableOp {

    abstract val armRepr: String

    abstract val x86ByteAddr: Array<String>

    override fun toArm() = armRepr
    override fun tox86() = x86ByteAddr[2]

    fun x86AddressByte(numBytes: Int): String {
        val arr = arrayOf(1, 2, 4)
        if (numBytes !in arr) {
            throw UnsupportedOperationException("Invalid attempt to access register of $numBytes bytes")
        }

        return x86ByteAddr[arr.indexOf(numBytes)]
    }

    object SP : Register() {
        override val armRepr = "sp"
        override val x86ByteAddr = arrayOf("spl", "sp", "esp")
    }

    object R0 : Register() {
        override val armRepr = "r0"
        override val x86ByteAddr = arrayOf("al", "ax", "eax")
    }

    object R1 : Register() {
        override val armRepr = "r1"
        override val x86ByteAddr = arrayOf("bl", "bx", "ebx")
    }

    object R2 : Register() {
        override val armRepr = "r2"
        override val x86ByteAddr = arrayOf("cl", "cx", "ecx")
    }

    object R3 : Register() {
        override val armRepr = "r3"
        override val x86ByteAddr = arrayOf("dl", "dx", "edx")
    }

    object R4 : Register() {
        override val armRepr = "r4"
        override val x86ByteAddr = arrayOf("cl", "cx", "ecx")
    }

}