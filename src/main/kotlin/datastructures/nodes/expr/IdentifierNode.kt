package datastructures.nodes.expr

import datastructures.SymbolTable
import analyser.exceptions.SemanticsException
import datastructures.nodes.assignment.AccessMode
import datastructures.nodes.assignment.LHSNode
import datastructures.nodes.function.FuncNode
import datastructures.type.Type
import datastructures.type.VoidType
import generator.translator.CodeGeneratorVisitor
import org.antlr.v4.runtime.ParserRuleContext

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

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateIdentifier(this)
    }

}