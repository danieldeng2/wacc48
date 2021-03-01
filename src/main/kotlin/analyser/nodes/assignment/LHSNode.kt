package analyser.nodes.assignment

import analyser.nodes.ASTNode
import analyser.nodes.type.Typable
import analyser.nodes.type.Type
import generator.instructions.arithmetic.ADDInstr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.translator.storeLocalVar

enum class AccessMode {
    ASSIGN, READ, ADDRESS
}

interface LHSNode : ASTNode, Typable {
    var mode: AccessMode
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