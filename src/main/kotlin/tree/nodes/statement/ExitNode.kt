package tree.nodes.statement

import analyser.exceptions.SemanticsException
import tree.SymbolTable
import tree.nodes.expr.ExprNode
import tree.nodes.function.FuncNode
import tree.type.IntType
import org.antlr.v4.runtime.ParserRuleContext
import tree.ASTVisitor

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
