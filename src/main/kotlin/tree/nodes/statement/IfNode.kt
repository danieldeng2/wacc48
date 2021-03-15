package tree.nodes.statement

import analyser.exceptions.SemanticsException
import org.antlr.v4.runtime.ParserRuleContext
import tree.ASTVisitor
import tree.SymbolTable
import tree.nodes.expr.ExprNode
import tree.nodes.function.FuncNode
import tree.type.BoolType

data class IfNode(
    var proposition: ExprNode,
    var trueStat: StatNode,
    var falseStat: StatNode,
    val ctx: ParserRuleContext?,
) : StatNode {
    lateinit var trueST: SymbolTable
    lateinit var falseST: SymbolTable

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.trueST = SymbolTable(st)
        this.falseST = SymbolTable(st)

        if (proposition.type != BoolType)
            throw SemanticsException(
                "If statement proposition must be boolean",
                ctx
            )

        proposition.validate(st, funTable)
        trueStat.validate(trueST, funTable)
        falseStat.validate(falseST, funTable)
    }

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitIf(this)
    }
}