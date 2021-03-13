package generator.instructions.load

import generator.instructions.Instruction
import generator.instructions.operands.LabelOp
import generator.instructions.operands.LoadableOp
import generator.instructions.operands.MemAddr
import generator.instructions.operands.Register
import generator.translator.ArmConstants

class LDREQInstr(
    private val reg: Register,
    private val op: LoadableOp
) : Instruction {

    companion object {
        private var counter = 0
    }

    override fun tox86() = when (op) {
        !is LabelOp -> listOf("\tcmove ${reg.tox86()}, ${op.tox86()}")
        else ->
            listOf(
                "\tjne __LDREQ$counter",
                "\tmov ${reg.tox86()}, ${op.tox86()}",
                "__LDREQ${counter++}:"
            )
    }

    override fun toArm() = "\tLDREQ ${reg.toArm()}, ${op.toArm()}"
}