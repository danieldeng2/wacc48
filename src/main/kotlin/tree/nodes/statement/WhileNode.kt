package tree.nodes.statement

import analyser.exceptions.SemanticsException
import org.antlr.v4.runtime.ParserRuleContext
import tree.ASTVisitor
import tree.SymbolTable
import tree.nodes.expr.ExprNode
import tree.nodes.function.FuncNode
import tree.type.BoolType

data class WhileNode(
    var proposition: ExprNode,
    var body: StatNode,
    val ctx: ParserRuleContext?,
) : StatNode {
    lateinit var bodyST: SymbolTable

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.bodyST = SymbolTable(st)
        proposition.validate(st, funTable)

        if (proposition.type != BoolType)
            throw SemanticsException("While statement proposition must be boolean", ctx)

        body.validate(bodyST, funTable)
    }

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitWhile(this)
    }

}

