package tree.nodes.function

import analyser.exceptions.SemanticsException
import org.antlr.v4.runtime.ParserRuleContext
import tree.ASTVisitor
import tree.SymbolTable
import tree.nodes.ASTNode
import tree.type.Typable
import tree.type.Type

data class ParamNode(
    override var type: Type,
    val text: String,
    val ctx: ParserRuleContext?
) : ASTNode, Typable {
    lateinit var st: SymbolTable

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

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitParam(this)
    }

}