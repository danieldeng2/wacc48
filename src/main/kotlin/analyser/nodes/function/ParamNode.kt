package analyser.nodes.function

import analyser.SymbolTable
import analyser.nodes.type.Type
import analyser.nodes.ASTNode
import analyser.nodes.type.Typable
import exceptions.SemanticsException
import org.antlr.v4.runtime.ParserRuleContext

data class ParamNode(
    override var type: Type,
    val text: String,
    override val ctx: ParserRuleContext?
) : ASTNode, Typable {
    override lateinit var st: SymbolTable
    override lateinit var funTable: SymbolTable

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        this.st = st
        this.funTable = funTable
        if (st.containsInCurrentScope(text))
            throw SemanticsException("Illegal re-declaration of parameter $text", ctx)
        st.add(text, this)
    }
}