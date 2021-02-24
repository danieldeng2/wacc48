package generator.translator

import analyser.nodes.type.*
import generator.armInstructions.STRBInstr
import generator.armInstructions.STRInstr
import generator.armInstructions.operands.MemAddr
import generator.armInstructions.operands.NumOp
import generator.armInstructions.operands.Register

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
