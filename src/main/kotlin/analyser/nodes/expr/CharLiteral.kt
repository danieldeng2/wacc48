package analyser.nodes.expr

import analyser.SymbolTable
import analyser.nodes.function.FuncNode
import analyser.nodes.type.CharType
import analyser.nodes.type.Type
import generator.instructions.Instruction
import generator.instructions.move.MOVInstr
import generator.instructions.operands.CharOp
import generator.instructions.operands.Register
import generator.translator.TranslatorContext
import org.antlr.v4.runtime.ParserRuleContext

data class CharLiteral(val value: Char, val ctx: ParserRuleContext?) :
    ExprNode {
    override var type: Type = CharType

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
    }

    override fun translate(ctx: TranslatorContext): List<Instruction> =
        listOf(MOVInstr(Register.R0, CharOp(value)))

}