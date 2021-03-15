package tree.nodes.statement

import analyser.exceptions.SemanticsException
import org.antlr.v4.runtime.ParserRuleContext
import tree.ASTVisitor
import tree.SymbolTable
import tree.nodes.assignment.AccessMode
import tree.nodes.assignment.LHSNode
import tree.nodes.function.FuncNode
import tree.type.CharType
import tree.type.IntType
import tree.type.StringType
import tree.type.Type

data class ReadNode(
    val value: LHSNode,
    val ctx: ParserRuleContext?,
) : StatNode {
    private val expectedExprTypes: List<Type> = listOf(IntType, StringType, CharType)

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {

        value.mode = AccessMode.ADDRESS
        value.validate(st, funTable)
        if (value.type !in expectedExprTypes)
            throw SemanticsException("Cannot read from type ${value.type}", ctx)
    }

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitRead(this)
    }
}