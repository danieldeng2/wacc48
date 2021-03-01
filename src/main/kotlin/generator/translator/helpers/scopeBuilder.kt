package generator.translator.helpers

import analyser.SymbolTable
import generator.instructions.Instruction
import generator.instructions.arithmetic.ADDInstr
import generator.instructions.arithmetic.SUBInstr
import generator.instructions.directives.LabelInstr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.instructions.stack.POPInstr
import generator.instructions.stack.PUSHInstr

fun MutableList<Instruction>.newScope(
    st: SymbolTable,
    bodyBuilder: MutableList<Instruction>.() -> Unit
) {
    val maxImmediateValue = 1024
    val localVarSize = st.totalVarSize

    for (size in localVarSize downTo 1 step maxImmediateValue) {
        add(
            SUBInstr(
                Register.SP,
                Register.SP,
                NumOp(minOf(size, maxImmediateValue))
            )
        )
    }

    bodyBuilder()

    for (size in localVarSize downTo 1 step maxImmediateValue) {
        add(
            ADDInstr(
                Register.SP,
                Register.SP,
                NumOp(minOf(size, maxImmediateValue))
            )
        )
    }
}

fun MutableList<Instruction>.declareFunction(
    identifier: String,
    bodyBuilder: MutableList<Instruction>.() -> Unit
) {
    add(LabelInstr(identifier))
    add(PUSHInstr(Register.LR))

    bodyBuilder()

    add(POPInstr(Register.PC))
}
