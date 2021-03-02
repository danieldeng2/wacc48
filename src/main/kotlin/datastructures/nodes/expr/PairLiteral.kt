package datastructures.nodes.expr

import datastructures.SymbolTable
import datastructures.nodes.function.FuncNode
import datastructures.type.EmptyPair
import datastructures.type.Type
import generator.instructions.move.MOVInstr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.translator.TranslatorContext

object PairLiteral : ExprNode {
    override var type: Type = EmptyPair

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
    }

    override fun toString(): String {
        return "Null"
    }

    override fun translate(ctx: TranslatorContext) =
        listOf(MOVInstr(Register.R0, NumOp(0)))
}
