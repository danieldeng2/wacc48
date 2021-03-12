package generator.instructions.load

import generator.instructions.Instruction
import generator.instructions.operands.LabelOp
import generator.instructions.operands.LoadableOp
import generator.instructions.operands.MemAddr
import generator.instructions.operands.Register
import generator.translator.ArmConstants

class LDRLTInstr(val rd: Register, val op: LoadableOp) : Instruction {

    override fun tox86() = when (op) {
        !is LabelOp -> listOf("\tcmovlt ${rd.tox86()}, ${op.tox86()}")
        else ->
            listOf(
                "\tpush ${rd.tox86()}",
                "\tmov ${rd.tox86()}, ${op.tox86()}",
                "\tcmovge ${rd.tox86()}, ${MemAddr(Register.SP).tox86()}",
                "\tadd ${Register.SP.tox86()}, ${ArmConstants.NUM_BYTE_ADDRESS}"
            )
    }

    override fun toArm() = "\tLDRLT ${rd.toArm()}, ${op.toArm()}"
}
