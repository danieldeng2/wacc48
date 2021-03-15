package wacc48.generator.translator.helpers

import wacc48.generator.instructions.Instruction
import wacc48.generator.instructions.arithmetic.ADDInstr
import wacc48.generator.instructions.arithmetic.SUBInstr
import wacc48.generator.instructions.operands.NumOp
import wacc48.generator.instructions.operands.Register
import wacc48.generator.translator.ArmConstants.OPERAND2_MAX_VALUE
import wacc48.tree.SymbolTable

/** Wraps a list of instruction inside stack manipulation instructions to move
 * stack pointer [Register.SP] up or down. */
fun MutableList<Instruction>.newScope(
    st: SymbolTable,
    bodyBuilder: MutableList<Instruction>.() -> Unit
) {
    startScope(st)
    bodyBuilder()
    endScope(st)
}

/** Moves the stack pointer down by the size of the reserved stack space
 * for local variables inside the scope. This can be split over
 * multiple multiple SUB instructions as the operand2 field only has
 * a limited number of bits. */
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

/** Moves the stack pointer up by the size of the reserved stack space
 * for local variables inside the current scope. This can be split over
 * multiple multiple ADD instructions as the operand2 field only has
 * a limited number of bits. */
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

/** Helper function for 'return' statement. This moves the stack pointer up
 * by the amount equivalent to all the stack space reserved for current function
 * thus far. This includes everything up to the scope for the
 * arguments for the function.*/
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

