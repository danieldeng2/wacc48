package analyser.nodes.expr

import analyser.SymbolTable
import analyser.nodes.function.FuncNode
import analyser.nodes.type.BoolType
import analyser.nodes.type.Type
import generator.instructions.move.MOVInstr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.translator.TranslatorContext
import org.antlr.v4.runtime.ParserRuleContext

data class BoolLiteral(
    val value: Boolean,
    override val ctx: ParserRuleContext?
) : ExprNode {
    override var type: Type = BoolType
    override lateinit var st: SymbolTable


    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.st = st
    }

    override fun translate(ctx: TranslatorContext) =
        when (value) {
            true -> listOf(MOVInstr(Register.R0, NumOp(1)))
            false -> listOf(MOVInstr(Register.R0, NumOp(0)))
        }
}