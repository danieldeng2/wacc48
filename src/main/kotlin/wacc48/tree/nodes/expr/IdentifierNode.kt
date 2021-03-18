package wacc48.tree.nodes.expr

import org.antlr.v4.runtime.ParserRuleContext
import wacc48.analyser.exceptions.Issue
import wacc48.analyser.exceptions.addSemantic
import wacc48.shell.MemoryTable
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.ASTNode
import wacc48.tree.nodes.assignment.AccessMode
import wacc48.tree.nodes.assignment.LHSNode
import wacc48.tree.nodes.function.FuncNode
import wacc48.tree.type.Type
import wacc48.tree.type.VoidType

data class IdentifierNode(
    val name: String,
    val ctx: ParserRuleContext?,
) : LHSNode, ExprNode {
    override var type: Type = VoidType
    lateinit var st: SymbolTable
    override var mode: AccessMode = AccessMode.READ

    override fun reduceToLiteral(mt: MemoryTable?): Literal {
        return mt?.getLiteral(name) ?: StringLiteral("<Undefined value>", null)
    }

    override val children: List<ASTNode>
        get() = emptyList()

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>,
        issues: MutableList<Issue>
    ) {
        this.st = st
        if (!st.containsInAnyScope(name)) {
            issues.addSemantic("Unknown identifier $name", ctx)
            return
        }

        type = st[name]!!
    }

    override fun <T> acceptVisitor(visitor: ASTVisitor<T>): T {
        return visitor.visitIdentifier(this)
    }

}