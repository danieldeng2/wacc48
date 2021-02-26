package generator.instructions

class BLVSInstr(val label: String) : Instruction {

    override fun toString() = "\tBLVS $label"
}
