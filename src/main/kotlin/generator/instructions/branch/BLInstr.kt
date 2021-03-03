package generator.instructions.branch

import generator.instructions.Instruction

class BLInstr(val name: String) : Instruction {
    override fun toString() = "\tBL $name"
}
