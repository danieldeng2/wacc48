package wacc48.tree.nodes.expr

import wacc48.analyser.exceptions.SemanticsException
import org.antlr.v4.runtime.ParserRuleContext
import wacc48.shell.MemoryTable
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
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

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.st = st
        if (!st.containsInAnyScope(name))
            throw SemanticsException("Unknown identifier $name", ctx)

        type = st[name]!!
    }

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitIdentifier(this)
    }

}