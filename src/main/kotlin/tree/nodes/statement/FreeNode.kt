package tree.nodes.statement

import analyser.exceptions.SemanticsException
import org.antlr.v4.runtime.ParserRuleContext
import tree.ASTVisitor
import tree.SymbolTable
import tree.nodes.expr.ExprNode
import tree.nodes.function.FuncNode
import tree.type.GenericPair

data class FreeNode(
    var value: ExprNode,
    val ctx: ParserRuleContext?,
) : StatNode {

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        value.validate(st, funTable)

        if (value.type !is GenericPair)
            throw SemanticsException("Cannot free ${value.type}", ctx)
    }

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitFree(this)
    }

}