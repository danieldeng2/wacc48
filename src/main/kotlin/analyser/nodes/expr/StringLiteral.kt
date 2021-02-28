package analyser.nodes.expr

import analyser.SymbolTable
import analyser.nodes.function.FuncNode
import analyser.nodes.type.StringType
import analyser.nodes.type.Type
import generator.instructions.load.LDRInstr
import generator.instructions.operands.LabelOp
import generator.instructions.operands.Register
import generator.translator.TranslatorContext
import org.antlr.v4.runtime.ParserRuleContext


data class StringLiteral(
    val value: String,
    override val ctx: ParserRuleContext?
) : ExprNode {
    override lateinit var st: SymbolTable
    override lateinit var funTable: MutableMap<String, FuncNode>

    override var type: Type = StringType

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.st = st
        this.funTable = funTable
    }

    override fun translate(ctx: TranslatorContext) = listOf(
        LDRInstr(
            Register.R0,
            LabelOp(ctx.addMessage(value))
        )
    )
}