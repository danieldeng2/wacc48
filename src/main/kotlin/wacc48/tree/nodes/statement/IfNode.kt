package wacc48.tree.nodes.statement

import org.antlr.v4.runtime.ParserRuleContext
import wacc48.analyser.exceptions.Issue
import wacc48.analyser.exceptions.addSemantic
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.expr.ExprNode
import wacc48.tree.nodes.function.FuncNode
import wacc48.tree.type.BoolType

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
        funTable: MutableMap<String, FuncNode>,
        issues: MutableList<Issue>
    ) {
        this.trueST = SymbolTable(st)
        this.falseST = SymbolTable(st)

        if (proposition.type != BoolType)
            issues.addSemantic(
                "If statement proposition must be boolean",
                ctx
            )

        proposition.validate(st, funTable, issues)
        trueStat.validate(trueST, funTable, issues)
        falseStat.validate(falseST, funTable, issues)
    }

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitIf(this)
    }
}