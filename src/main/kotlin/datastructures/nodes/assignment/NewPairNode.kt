package datastructures.nodes.assignment

import datastructures.SymbolTable
import datastructures.nodes.expr.ExprNode
import datastructures.nodes.function.FuncNode
import datastructures.type.PairType
import datastructures.type.Type
import datastructures.type.VoidType
import generator.instructions.Instruction
import generator.instructions.branch.BLInstr
import generator.instructions.move.MOVInstr
import generator.instructions.operands.MemAddr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.instructions.store.STRInstr
import generator.translator.CodeGeneratorVisitor
import generator.translator.TranslatorContext
import generator.translator.helpers.*
import org.antlr.v4.runtime.ParserRuleContext

data class NewPairNode(
    val firstElem: ExprNode,
    val secondElem: ExprNode,
    val ctx: ParserRuleContext?
) : RHSNode {
    override var type: Type = VoidType


    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {

        firstElem.validate(st, funTable)
        secondElem.validate(st, funTable)

        type = PairType(firstElem.type, secondElem.type, ctx)
    }

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateNewPair(this)
    }

}