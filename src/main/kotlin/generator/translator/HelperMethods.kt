package generator.translator

import analyser.SymbolTable
import analyser.nodes.type.*
import generator.instructions.Instruction
import generator.instructions.arithmetic.ADDInstr
import generator.instructions.arithmetic.SUBInstr
import generator.instructions.load.LDRInstr
import generator.instructions.load.LDRSBInstr
import generator.instructions.operands.MemAddr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.instructions.stack.POPInstr
import generator.instructions.stack.PUSHInstr
import generator.instructions.store.STRBInstr
import generator.instructions.store.STRInstr

fun storeLocalVar(
    varType: Type,
    stackOffset: Int,
    rd: Register = Register.SP,
    rn: Register = Register.R0
) =
    when (varType) {
        is BoolType, CharType ->
            STRBInstr(rn, MemAddr(rd, NumOp(stackOffset)))

        is IntType, StringType, is GenericPair, is ArrayType ->
            STRInstr(rn, MemAddr(rd, NumOp(stackOffset)))

        else -> throw UnknownError("Cannot store $varType")
    }

fun loadLocalVar(
    varType: Type,
    stackOffset: Int,
    rn: Register = Register.SP,
    rd: Register = Register.R0
) =
    when (varType) {
        is BoolType, CharType ->
            LDRSBInstr(rd, MemAddr(rn, NumOp(stackOffset)))

        is IntType, StringType, is GenericPair, is ArrayType ->
            LDRInstr(rd, MemAddr(rn, NumOp(stackOffset)))

        else -> throw UnknownError("Cannot load $varType")
    }

fun MutableList<Instruction>.newScope(
    st: SymbolTable,
    bodyBuilder: MutableList<Instruction>.() -> Unit
) {
    val maxImmediateValue = 1024
    val localStackSize = st.totalVarSize

    for (size in localStackSize downTo 1 step maxImmediateValue) {
        add(
            SUBInstr(
                Register.SP,
                Register.SP,
                NumOp(minOf(size, maxImmediateValue))
            )
        )
    }

    bodyBuilder()

    for (size in localStackSize downTo 1 step maxImmediateValue) {
        add(
            ADDInstr(
                Register.SP,
                Register.SP,
                NumOp(minOf(size, maxImmediateValue))
            )
        )
    }
}


fun pushAndIncrement(
    ctx: TranslatorContext,
    vararg registers: Register
): PUSHInstr {
    ctx.stackPtrOffset += 4 * registers.size
    return PUSHInstr(*registers)
}

fun popAndDecrement(
    ctx: TranslatorContext,
    vararg registers: Register
): POPInstr {
    ctx.stackPtrOffset -= 4 * registers.size
    return POPInstr(*registers)
}