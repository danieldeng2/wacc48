package wacc48.tree.nodes.statement

import wacc48.analyser.exceptions.SemanticsException
import org.antlr.v4.runtime.ParserRuleContext
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.expr.ExprNode
import wacc48.tree.nodes.function.FuncNode
import wacc48.tree.type.GenericPair

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