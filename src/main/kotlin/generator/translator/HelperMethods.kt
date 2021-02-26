package generator.translator

import analyser.nodes.type.*
import generator.instructions.load.LDRInstr
import generator.instructions.load.LDRSBInstr
import generator.instructions.operands.MemAddr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.instructions.stack.POPInstr
import generator.instructions.stack.PUSHInstr
import generator.instructions.store.STRBInstr
import generator.instructions.store.STRInstr

fun storeLocalVar(varType: Type, stackOffset: Int) =
    when (varType) {
        is BoolType, CharType ->
            STRBInstr(
                Register.R0,
                MemAddr(Register.SP, NumOp(stackOffset))
            )

        is IntType, StringType ->
            STRInstr(Register.R0, MemAddr(Register.SP, NumOp(stackOffset)))

        else -> TODO("Store other types")
    }

fun loadLocalVar(varType: Type, stackOffset: Int) =
    when (varType) {
        is BoolType, CharType ->
            LDRSBInstr(
                Register.R0,
                MemAddr(Register.SP, NumOp(stackOffset))
            )

        is IntType, StringType ->
            LDRInstr(Register.R0, MemAddr(Register.SP, NumOp(stackOffset)))

        else -> TODO("Store other types")
    }

fun pushAndIncrement(reg: Register, ctx: TranslatorContext): PUSHInstr {
    ctx.stackPtrOffset += 4
    return PUSHInstr(reg)
}

fun popAndDecrement(reg: Register, ctx: TranslatorContext): POPInstr {
    ctx.stackPtrOffset -= 4
    return POPInstr(reg)
}