package analyser.nodes.expr

import analyser.SymbolTable
import analyser.nodes.type.EmptyPair
import analyser.nodes.type.Type
import generator.instructions.Instruction
import generator.instructions.move.MOVInstr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.translator.TranslatorContext
import org.antlr.v4.runtime.ParserRuleContext

object PairLiteral : ExprNode {
    override lateinit var st: SymbolTable
    override lateinit var funTable: SymbolTable

    override var type: Type = EmptyPair
    override val ctx: ParserRuleContext? = null

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        this.st = st
        this.funTable = funTable
    }

    override fun toString(): String {
        return "Null"
    }

    override fun translate(ctx: TranslatorContext) =
        listOf(MOVInstr(Register.R0, NumOp(0)))
}
