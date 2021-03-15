package generator.instructions.load

import generator.instructions.Instruction
import generator.instructions.operands.LabelOp
import generator.instructions.operands.LoadableOp
import generator.instructions.operands.Register

class LDRCSInstr(val rd: Register, val op: LoadableOp) :
    Instruction {

    companion object {
        private var counter = 0
    }

    override fun tox86() = when (op) {
        !is LabelOp -> listOf("\tcmovb ${rd.tox86()}, ${op.tox86()}")
        else ->
            listOf(
                "\tjb __LDRCS${counter}",
                "\tmov ${rd.tox86()}, ${op.tox86()}",
                "__LDRCS${counter++}:"
            )
    }
    override fun toArm() = "\tLDRCS ${rd.toArm()}, ${op.toArm()}"
}
