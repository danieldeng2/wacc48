package generator.translator

import analyser.nodes.type.*
import generator.instructions.arithmetic.ADDInstr
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

        is IntType, StringType, is GenericPair ->
            STRInstr(rn, MemAddr(rd, NumOp(stackOffset)))

        else -> TODO("Store $varType type")
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

        is IntType, StringType, is GenericPair ->
            LDRInstr(rd, MemAddr(rn, NumOp(stackOffset)))

        else -> TODO("Load $varType type")
    }

fun addressVar(
    stackOffset: Int,
    rn: Register = Register.SP,
    rd: Register = Register.R0
) =
    ADDInstr(rd, rn, NumOp(stackOffset))


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