package datastructures.nodes.function

import analyser.exceptions.SemanticsException
import datastructures.SymbolTable
import datastructures.nodes.ASTNode
import datastructures.type.Typable
import datastructures.type.Type
import generator.instructions.Instruction
import generator.translator.TranslatorContext
import org.antlr.v4.runtime.ParserRuleContext

data class ParamNode(
    override var type: Type,
    val text: String,
    val ctx: ParserRuleContext?
) : ASTNode, Typable {
    private lateinit var st: SymbolTable

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.st = st
        if (st.containsInCurrentScope(text))
            throw SemanticsException(
                "Illegal re-declaration of parameter $text",
                ctx
            )
        st[text] = type
    }

    override fun translate(ctx: TranslatorContext): List<Instruction> {
        st.declareVariable(text)
        return emptyList()
    }

}