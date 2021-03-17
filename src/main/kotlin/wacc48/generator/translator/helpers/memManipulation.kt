package wacc48.generator.translator.helpers

import wacc48.generator.instructions.Instruction
import wacc48.generator.instructions.load.LDRInstr
import wacc48.generator.instructions.load.LDRSBInstr
import wacc48.generator.instructions.operands.ArgumentAddr
import wacc48.generator.instructions.operands.MemAddr
import wacc48.generator.instructions.operands.NumOp
import wacc48.generator.instructions.operands.Register
import wacc48.generator.instructions.store.STRBInstr
import wacc48.generator.instructions.store.STRInstr
import wacc48.tree.type.*

/** Selects the correct 'store' instruction based on the size of [varType].
 *
 * Stores the value in [rn] into the memory location calculated by
 * adding the value inside [rd] with [stackOffset].
 */
fun storeLocalVar(
    varType: Type,
    stackOffset: Int,
    rd: Register = Register.SP,
    rn: Register = Register.R0,
    isArgLoad: Boolean = false,
    isArgument: Boolean = false
): Instruction {

    val memAddrLocation =
        if (isArgument)
            ArgumentAddr(rd, NumOp(stackOffset), isArgLoad)
        else
            MemAddr(rd, NumOp(stackOffset), isArgLoad)

    return when (varType) {
        is BoolType, CharType ->
            STRBInstr(rn, memAddrLocation)

        is IntType, StringType, is GenericPair, is ArrayType ->
            STRInstr(rn, memAddrLocation)

        else -> throw UnknownError("Cannot store $varType")
    }
}

/** Selects the correct 'load' instruction based on the size of [varType].
 *
 * Loads the value at memory location calculated by
 * adding the value inside [rn] with [stackOffset] into register [rd].
 */
fun loadLocalVar(
    varType: Type,
    stackOffset: Int,
    rn: Register = Register.SP,
    rd: Register = Register.R0,
    isArgument: Boolean = false
): Instruction {

    val memAddrLocation =
        if (isArgument)
            ArgumentAddr(rn, NumOp(stackOffset))
        else
            MemAddr(rn, NumOp(stackOffset))

    return when (varType) {
        is BoolType, CharType ->
            LDRSBInstr(rd, memAddrLocation)

        is IntType, StringType, is GenericPair, is ArrayType ->
            LDRInstr(rd, memAddrLocation)

        else -> throw UnknownError("Cannot load $varType")
    }
}