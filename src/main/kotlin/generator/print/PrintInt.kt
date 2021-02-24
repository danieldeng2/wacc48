package generator.print

import generator.armInstructions.*
import generator.armInstructions.directives.Ascii
import generator.armInstructions.directives.Word
import generator.armInstructions.operands.LabelOp
import generator.armInstructions.operands.NumOp
import generator.armInstructions.operands.Register

object PrintInt : PrintSyscall {

    override fun translate(msgIndex: Int) =
        mutableListOf<Instruction>().apply {
            add(LabelInstr("p_print_int"))
            add(PUSHInstr(Register.LR))
            add(MOVInstr(Register.R1, Register.R0))
            add(LDRInstr(Register.R0, LabelOp(msgIndex)))
            add(ADDInstr(Register.R0, Register.R0, NumOp(4)))
            add(BLInstr("printf"))
            add(MOVInstr(Register.R0, NumOp(0)))
            add(BLInstr("fflush"))
            add(POPInstr(Register.PC))
        }

}