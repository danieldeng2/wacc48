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
    private lateinit var st: SymbolTable

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.st = st
        params.asReversed().forEach {
            it.validate(st, funTable)
        }
    }

    override fun translate(ctx: TranslatorContext): List<Instruction> {
        params.forEach {
            st.declareVariable(it.text)
        }
        return emptyList()
    }
}
