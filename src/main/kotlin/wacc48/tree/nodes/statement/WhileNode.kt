package wacc48.tree.nodes.statement

import org.antlr.v4.runtime.ParserRuleContext
import wacc48.analyser.exceptions.Issue
import wacc48.analyser.exceptions.addSemantic
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.expr.ExprNode
import wacc48.tree.nodes.function.FuncNode
import wacc48.tree.type.BoolType

data class WhileNode(
    var proposition: ExprNode,
    var body: StatNode,
    val ctx: ParserRuleContext?,
) : StatNode {
    lateinit var bodyST: SymbolTable

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>,
        issues: MutableList<Issue>
    ) {
        this.bodyST = SymbolTable(st)
        proposition.validate(st, funTable, issues)

        if (proposition.type != BoolType)
            issues.addSemantic("While statement proposition must be boolean", ctx)

        body.validate(bodyST, funTable, issues)
    }

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitWhile(this)
    }

}

