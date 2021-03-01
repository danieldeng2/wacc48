package analyser.nodes.expr

import analyser.SymbolTable
import analyser.nodes.function.FuncNode
import analyser.nodes.type.EmptyPair
import analyser.nodes.type.Type
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
