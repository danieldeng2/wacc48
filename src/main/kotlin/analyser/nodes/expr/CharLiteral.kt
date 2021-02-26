package analyser.nodes.expr

import analyser.SymbolTable
import analyser.nodes.type.CharType
import analyser.nodes.type.Type
import generator.translator.TranslatorContext
import generator.instructions.Instruction
import generator.instructions.MOVInstr
import generator.instructions.operands.CharOp
import generator.instructions.operands.Register
import org.antlr.v4.runtime.ParserRuleContext

data class CharLiteral(val value: Char, override val ctx: ParserRuleContext?) :
    ExprNode {
    override var type: Type = CharType
    override lateinit var st: SymbolTable
    override lateinit var funTable: SymbolTable

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        this.st = st
        this.funTable = funTable
    }

    override fun translate(ctx: TranslatorContext): List<Instruction> =
        listOf(MOVInstr(Register.R0, CharOp(value)))

}