package generator.instructions

class BLInstr(val name: String) : Instruction {
    override fun toString() = "\tBL $name"
}
