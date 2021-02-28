package analyser.nodes.function

import analyser.SymbolTable
import analyser.nodes.ASTNode
import analyser.nodes.type.Typable
import analyser.nodes.type.Type
import exceptions.SemanticsException
import generator.instructions.Instruction
import generator.translator.TranslatorContext
import generator.translator.storeLocalVar
import org.antlr.v4.runtime.ParserRuleContext

data class ParamNode(
    override var type: Type,
    val text: String,
    override val ctx: ParserRuleContext?
) : ASTNode, Typable {
    override lateinit var st: SymbolTable
    override lateinit var funTable: MutableMap<String, FuncNode>

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.st = st
        this.funTable = funTable
        if (st.containsInCurrentScope(text))
            throw SemanticsException(
                "Illegal re-declaration of parameter $text",
                ctx
            )
        st[text] = type
    }

    override fun translate(ctx: TranslatorContext): List<Instruction> {
        st.declareVariable(text)
        val offset = ctx.getOffsetOfVar(text, st)
        return listOf(storeLocalVar(type, offset))
    }

}