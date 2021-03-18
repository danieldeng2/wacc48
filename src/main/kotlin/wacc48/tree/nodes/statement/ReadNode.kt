package wacc48.tree.nodes.statement

import org.antlr.v4.runtime.ParserRuleContext
import wacc48.analyser.exceptions.Issue
import wacc48.analyser.exceptions.addSemantic
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.ASTNode
import wacc48.tree.nodes.assignment.AccessMode
import wacc48.tree.nodes.assignment.LHSNode
import wacc48.tree.nodes.function.FuncNode
import wacc48.tree.type.CharType
import wacc48.tree.type.IntType
import wacc48.tree.type.StringType
import wacc48.tree.type.Type

data class ReadNode(
    val value: LHSNode,
    val ctx: ParserRuleContext?,
) : StatNode {

    private val expectedExprTypes: List<Type> =
        listOf(IntType, StringType, CharType)

    override val children: List<ASTNode>
        get() = listOf(value)

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>,
        issues: MutableList<Issue>
    ) {

        value.mode = AccessMode.ADDRESS
        value.validate(st, funTable, issues)
        if (value.type !in expectedExprTypes)
            issues.addSemantic("Cannot read from type ${value.type}", ctx)
    }

    override fun <T> acceptVisitor(visitor: ASTVisitor<T>): T {
        return visitor.visitRead(this)
    }
}