package wacc48.tree.nodes.expr

import org.antlr.v4.runtime.ParserRuleContext
import wacc48.analyser.exceptions.Issue
import wacc48.analyser.exceptions.addSyntax
import wacc48.shell.MemoryTable
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.ASTNode
import wacc48.tree.nodes.function.FuncNode
import wacc48.tree.type.IntType
import wacc48.tree.type.Type

data class IntLiteral(
    val value: Int,
    val isOutOfBounds: Boolean = false,
    val ctx: ParserRuleContext?
) : BaseLiteral {

    override var type: Type = IntType

    override fun literalToString(mt: MemoryTable?): String = value.toString()
    override val children: List<ASTNode>
        get() = emptyList()

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>,
        issues: MutableList<Issue>
    ) {
        if (isOutOfBounds)
            issues.addSyntax("IntLiteral $value is out of range", ctx)
    }

    override fun <T> acceptVisitor(visitor: ASTVisitor<T>): T {
        return visitor.visitIntLiteral(this)
    }
}