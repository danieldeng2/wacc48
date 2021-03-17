package wacc48.tree.nodes.expr

import org.antlr.v4.runtime.ParserRuleContext
import wacc48.analyser.exceptions.Issue
import wacc48.shell.MemoryTable
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.function.FuncNode
import wacc48.tree.type.StringType
import wacc48.tree.type.Type


data class StringLiteral(
    val value: String,
    val ctx: ParserRuleContext?
) : Literal {

     override var type: Type = StringType

    override fun literalToString(mt: MemoryTable?): String {
        return value
            .replace("\\n", "\n")
            .replace("\\0", "\u0000")
            .replace("\\b", "\b")
            .replace("\\t", "\t")
            .replace("\\f", "\u000c")
            .replace("\\r", "\r")
            .replace("\\\"", "\"")
            .replace("\\'", "'")
            .replace("\\\\", "\\")
    }

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>,
        issues: MutableList<Issue>
    ) {
    }

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitStringLiteral(this)
    }

}