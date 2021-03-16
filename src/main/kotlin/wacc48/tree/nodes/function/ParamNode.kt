package wacc48.tree.nodes.function

import org.antlr.v4.runtime.ParserRuleContext
import wacc48.analyser.exceptions.Issue
import wacc48.analyser.exceptions.addSemantic
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.ASTNode
import wacc48.tree.type.Typable
import wacc48.tree.type.Type

data class ParamNode(
    override var type: Type,
    val text: String,
    val ctx: ParserRuleContext?
) : ASTNode, Typable {
    lateinit var st: SymbolTable

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>,
        issues: MutableList<Issue>
    ) {
        this.st = st
        if (st.containsInCurrentScope(text)) {
            issues.addSemantic(
                "Illegal re-declaration of parameter $text",
                ctx
            )
            return
        }
        st[text] = type
    }

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitParam(this)
    }

}