package generator.translator.helpers

import analyser.nodes.assignment.AccessMode
import analyser.nodes.assignment.LHSNode
import analyser.nodes.type.*
import generator.instructions.arithmetic.ADDInstr
import generator.instructions.load.LDRInstr
import generator.instructions.load.LDRSBInstr
import generator.instructions.operands.MemAddr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
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

fun LHSNode.readOrAssign(
    varType: Type,
    stackOffset: Int,
    rn: Register,
    rd: Register
) =
    if (mode == AccessMode.ASSIGN)
        storeLocalVar(
            varType = varType,
            stackOffset = stackOffset,
            rn = rn,
            rd = rd
        )
    else
        ADDInstr(rn, rd, NumOp(0))