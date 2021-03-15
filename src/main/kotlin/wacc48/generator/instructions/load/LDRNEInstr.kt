package wacc48.generator.instructions.load

import wacc48.generator.instructions.Instruction
import wacc48.generator.instructions.operands.LabelOp
import wacc48.generator.instructions.operands.LoadableOp
import wacc48.generator.instructions.operands.Register

class LDRNEInstr(
    private val reg: Register,
    private val op: LoadableOp
) : Instruction {

    companion object {
        private var counter = 0
    }

    override fun tox86() = when (op) {
        !is LabelOp -> listOf("\tcmovne ${reg.tox86()}, ${op.tox86()}")
        else ->
            listOf(
                "\tje __LDRNE${counter}",
                "\tmov ${reg.tox86()}, ${op.tox86()}",
                "__LDRNE${counter++}:"
            )
    }

    override fun toArm() = "\tLDRNE ${reg.toArm()}, ${op.toArm()}"
}