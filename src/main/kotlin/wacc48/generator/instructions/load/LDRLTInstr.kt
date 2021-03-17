package wacc48.generator.instructions.load

import wacc48.generator.instructions.Instruction
import wacc48.generator.instructions.operands.LabelOp
import wacc48.generator.instructions.operands.LoadableOp
import wacc48.generator.instructions.operands.Register

class LDRLTInstr(val rd: Register, val op: LoadableOp) : Instruction {

    companion object {
        private var counter = 0
    }

    override fun tox86() = when (op) {
        !is LabelOp -> listOf("\tcmovlt ${rd.tox86()}, ${op.tox86()}")
        else ->
            listOf(
                "\tjge __LDRLT${counter}",
                "\tmov ${rd.tox86()}, ${op.tox86()}",
                "__LDRLT${counter++}:"
            )
    }

    override fun toArm() = "\tLDRLT ${rd.toArm()}, ${op.toArm()}"
}
