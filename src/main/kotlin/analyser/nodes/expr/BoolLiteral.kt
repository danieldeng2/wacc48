package analyser.nodes.expr

import analyser.SymbolTable
import analyser.nodes.type.BoolType
import analyser.nodes.type.Type
import generator.translator.TranslatorContext
import generator.armInstructions.MOVInstr
import generator.armInstructions.operands.NumOp
import generator.armInstructions.operands.Register
import org.antlr.v4.runtime.ParserRuleContext

data class BoolLiteral(
    val value: Boolean,
    override val ctx: ParserRuleContext?
) : ExprNode {
    override var type: Type = BoolType
    override lateinit var st: SymbolTable
    override lateinit var funTable: SymbolTable

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        this.st = st
        this.funTable = funTable
    }

    override fun translate(ctx: TranslatorContext) =
        when (value) {
            true -> listOf(MOVInstr(Register.R0, NumOp(1)))
            false -> listOf(MOVInstr(Register.R0, NumOp(0)))
        }
}