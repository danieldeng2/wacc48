package wacc48.tree.nodes.expr

import org.antlr.v4.runtime.ParserRuleContext
import wacc48.analyser.exceptions.Issue
import wacc48.shell.MemoryTable
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.ASTNode
import wacc48.tree.nodes.function.FuncNode
import wacc48.tree.type.CharType
import wacc48.tree.type.Type

data class CharLiteral(val value: Char, val ctx: ParserRuleContext?) :
    BaseLiteral {
    override var type: Type = CharType

    override fun literalToString(mt: MemoryTable?): String = value.toString()

    override val children: List<ASTNode>
        get() = emptyList()

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>,
        issues: MutableList<Issue>
    ) {
    }

    override fun <T> acceptVisitor(visitor: ASTVisitor<T>): T {
        return visitor.visitCharLiteral(this)
    }
}

