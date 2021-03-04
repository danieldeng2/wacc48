package generator.translator.helpers

import generator.instructions.load.LDRInstr
import generator.instructions.load.LDRSBInstr
import generator.instructions.operands.MemAddr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.instructions.store.STRBInstr
import generator.instructions.store.STRInstr
import tree.type.*

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
    isArgLoad: Boolean = false
) =
    when (varType) {
        is BoolType, CharType ->
            STRBInstr(rn, MemAddr(rd, NumOp(stackOffset), isArgLoad))

        is IntType, StringType, is GenericPair, is ArrayType ->
            STRInstr(rn, MemAddr(rd, NumOp(stackOffset), isArgLoad))

        else -> throw UnknownError("Cannot store $varType")
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
    rd: Register = Register.R0
) =
    when (varType) {
        is BoolType, CharType ->
            LDRSBInstr(rd, MemAddr(rn, NumOp(stackOffset)))

        is IntType, StringType, is GenericPair, is ArrayType ->
            LDRInstr(rd, MemAddr(rn, NumOp(stackOffset)))

        else -> throw UnknownError("Cannot load $varType")
    }
