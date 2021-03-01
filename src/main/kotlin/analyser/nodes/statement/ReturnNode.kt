package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.expr.ExprNode
import analyser.nodes.function.FuncNode
import generator.instructions.Instruction
import generator.instructions.operands.Register
import generator.instructions.stack.POPInstr
import generator.translator.TranslatorContext
import generator.translator.helpers.endScope
import org.antlr.v4.runtime.ParserRuleContext

data class ReturnNode(
    val value: ExprNode,
    val ctx: ParserRuleContext?,
) : StatNode {
    lateinit var bodyTable: SymbolTable

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        value.validate(st, funTable)
    }

    override fun translate(ctx: TranslatorContext) =
        mutableListOf<Instruction>().apply {
            addAll(value.translate(ctx))
            endScope(bodyTable)
            add(POPInstr(Register.PC))
        }
}