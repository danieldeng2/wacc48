package generator.instructions.load

import generator.instructions.Instruction
import generator.instructions.operands.LabelOp
import generator.instructions.operands.LoadableOp
import generator.instructions.operands.MemAddr
import generator.instructions.operands.Register
import generator.translator.ArmConstants
import generator.translator.ArmConstants.NUM_BYTE_ADDRESS

class LDRNEInstr(
    private val reg: Register,
    private val op: LoadableOp
) : Instruction {

    override fun tox86() = when (op) {
        !is LabelOp -> listOf("\tcmovne ${reg.tox86()}, ${op.tox86()}")
        else ->
            listOf(
                "\tpush ${reg.tox86()}",
                "\tmov ${reg.tox86()}, ${op.tox86()}",
                "\tcmove ${reg.tox86()}, ${MemAddr(Register.SP).tox86()}",
                "\tadd ${Register.SP.tox86()}, ${ArmConstants.NUM_BYTE_ADDRESS}"
            )
    }

    override fun toArm() = "\tLDRNE ${reg.toArm()}, ${op.toArm()}"
}