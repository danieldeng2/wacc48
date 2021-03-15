package wacc48.tree.nodes.statement

import wacc48.analyser.exceptions.SemanticsException
import org.antlr.v4.runtime.ParserRuleContext
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.expr.ExprNode
import wacc48.tree.nodes.function.FuncNode
import wacc48.tree.type.IntType

data class ExitNode(
    var expr: ExprNode,
    val ctx: ParserRuleContext?
) : StatNode {

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {

        expr.validate(st, funTable)
        if (expr.type != IntType)
            throw SemanticsException(
                "Exit must take integer as input, got ${expr.type} instead",
                ctx
            )
    }

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitExit(this)
    }

}
