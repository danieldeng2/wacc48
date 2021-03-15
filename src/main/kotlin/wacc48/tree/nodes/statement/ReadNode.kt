package wacc48.tree.nodes.statement

import wacc48.analyser.exceptions.SemanticsException
import org.antlr.v4.runtime.ParserRuleContext
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
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