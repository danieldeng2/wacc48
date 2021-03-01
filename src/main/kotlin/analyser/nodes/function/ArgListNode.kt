package analyser.nodes.function

import analyser.SymbolTable
import analyser.nodes.ASTNode
import analyser.nodes.expr.ExprNode
import generator.instructions.Instruction
import generator.translator.TranslatorContext
import org.antlr.v4.runtime.ParserRuleContext

data class ArgListNode(
    val args: List<ExprNode>,
    override val ctx: ParserRuleContext?
) : ASTNode {
    override lateinit var st: SymbolTable


    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.st = st
        args.forEach {
            it.validate(st, funTable)
        }
    }

    override fun translate(ctx: TranslatorContext): List<Instruction> {
        TODO("Not yet implemented")
    }
}