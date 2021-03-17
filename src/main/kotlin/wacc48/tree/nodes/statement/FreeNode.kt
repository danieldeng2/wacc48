package wacc48.tree.nodes.statement

import org.antlr.v4.runtime.ParserRuleContext
import wacc48.analyser.exceptions.Issue
import wacc48.analyser.exceptions.addSemantic
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
        funTable: MutableMap<String, FuncNode>,
        issues: MutableList<Issue>
    ) {
        value.validate(st, funTable, issues)

        if (value.type !is GenericPair)
            issues.addSemantic("Cannot free ${value.type}", ctx)
    }

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitFree(this)
    }

}