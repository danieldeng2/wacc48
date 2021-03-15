package tree.nodes.expr

import analyser.exceptions.SemanticsException
import tree.SymbolTable
import tree.nodes.assignment.AccessMode
import tree.nodes.assignment.LHSNode
import tree.nodes.function.FuncNode
import tree.type.Type
import tree.type.VoidType
import org.antlr.v4.runtime.ParserRuleContext
import tree.ASTVisitor

data class IdentifierNode(
    val name: String,
    val ctx: ParserRuleContext?,
) : LHSNode, ExprNode {
    override var type: Type = VoidType
    lateinit var st: SymbolTable
    override var mode: AccessMode = AccessMode.READ

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