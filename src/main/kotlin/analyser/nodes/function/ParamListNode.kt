package analyser.nodes.function

import analyser.SymbolTable
import analyser.nodes.ASTNode
import generator.instructions.Instruction
import generator.translator.TranslatorContext
import org.antlr.v4.runtime.ParserRuleContext

data class ParamListNode(
    val params: List<ParamNode>,
    override val ctx: ParserRuleContext?
) : ASTNode {
    override lateinit var st: SymbolTable


    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.st = st
        params.forEach {
            it.validate(st, funTable)
        }
    }

    override fun translate(ctx: TranslatorContext): List<Instruction> {
        TODO("Not yet implemented")
    }
}
