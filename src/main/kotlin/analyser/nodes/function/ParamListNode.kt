package analyser.nodes.function

import analyser.SymbolTable
import analyser.nodes.ASTNode
import generator.instructions.Instruction
import generator.translator.TranslatorContext
import org.antlr.v4.runtime.ParserRuleContext

data class ParamListNode(
    val params: List<ParamNode>,
    val ctx: ParserRuleContext?
) : ASTNode {


    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {

        params.forEach {
            it.validate(st, funTable)
        }
    }

    override fun translate(ctx: TranslatorContext): List<Instruction> {
        TODO("Not yet implemented")
    }
}
