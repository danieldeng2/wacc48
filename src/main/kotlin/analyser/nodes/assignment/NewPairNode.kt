package analyser.nodes.assignment

import analyser.SymbolTable
import analyser.nodes.expr.ExprNode
import analyser.nodes.function.FuncNode
import analyser.nodes.type.PairType
import analyser.nodes.type.Type
import analyser.nodes.type.VoidType
import generator.instructions.Instruction
import generator.instructions.branch.BLInstr
import generator.instructions.move.MOVInstr
import generator.instructions.operands.MemAddr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.instructions.store.STRInstr
import generator.translator.TranslatorContext
import generator.translator.popAndDecrement
import generator.translator.pushAndIncrement
import generator.translator.storeLocalVar
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

    override fun translate(ctx: TranslatorContext) =
        mutableListOf<Instruction>().apply {
            // Mallocs for 2 elements of pair
            addAll(storeElemInHeap(firstElem, ctx))
            addAll(storeElemInHeap(secondElem, ctx))

            // Malloc for the pair itself
            add(MOVInstr(Register.R0, NumOp(8)))
            add(BLInstr("malloc"))
            add(popAndDecrement(ctx, Register.R1, Register.R2))

            add(STRInstr(Register.R2, MemAddr(Register.R0)))
            add(STRInstr(Register.R1, MemAddr(Register.R0, NumOp(4))))
        }

    private fun storeElemInHeap(elem: ExprNode, ctx: TranslatorContext) =
        mutableListOf<Instruction>().apply {
            addAll(elem.translate(ctx))
            add(pushAndIncrement(ctx, Register.R0))
            add(MOVInstr(Register.R0, NumOp(elem.type.reserveStackSize)))
            add(BLInstr("malloc"))
            add(popAndDecrement(ctx, Register.R1))
            add(
                storeLocalVar(
                    varType = elem.type,
                    stackOffset = 0,
                    rn = Register.R1,
                    rd = Register.R0
                )
            )
            add(pushAndIncrement(ctx, Register.R0))
        }

}