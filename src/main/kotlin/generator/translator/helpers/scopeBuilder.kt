package generator.translator.helpers

import tree.SymbolTable
import generator.instructions.Instruction
import generator.instructions.arithmetic.ADDInstr
import generator.instructions.arithmetic.SUBInstr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.translator.ArmConstants.OPERAND2_MAX_VALUE

fun MutableList<Instruction>.newScope(
    st: SymbolTable,
    bodyBuilder: MutableList<Instruction>.() -> Unit
) {
    startScope(st)
    bodyBuilder()
    endScope(st)
}


fun MutableList<Instruction>.startScope(st: SymbolTable) {
    val localVarSize = st.totalVarSize

    for (size in localVarSize downTo 1 step OPERAND2_MAX_VALUE) {
        add(
            SUBInstr(
                Register.SP,
                Register.SP,
                NumOp(minOf(size, OPERAND2_MAX_VALUE))
            )
        )
    }
}

fun MutableList<Instruction>.endScope(st: SymbolTable) {
    val localVarSize = st.totalVarSize

    for (size in localVarSize downTo 1 step OPERAND2_MAX_VALUE) {
        add(
            ADDInstr(
                Register.SP,
                Register.SP,
                NumOp(minOf(size, OPERAND2_MAX_VALUE))
            )
        )
    }
}

fun MutableList<Instruction>.endAllScopes(st: SymbolTable) {
    val localVarSize = st.varSizeTotal()
    for (size in localVarSize downTo 1 step OPERAND2_MAX_VALUE) {
        add(
            ADDInstr(
                Register.SP,
                Register.SP,
                NumOp(minOf(size, OPERAND2_MAX_VALUE))
            )
        )
    }
}

