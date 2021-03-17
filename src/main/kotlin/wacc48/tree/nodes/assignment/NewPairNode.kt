package wacc48.tree.nodes.assignment

import org.antlr.v4.runtime.ParserRuleContext
import wacc48.analyser.exceptions.Issue
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.expr.ExprNode
import wacc48.tree.nodes.function.FuncNode
import wacc48.tree.type.PairType
import wacc48.tree.type.Type
import wacc48.tree.type.VoidType

data class NewPairNode(
    var firstElem: ExprNode,
    var secondElem: ExprNode,
    val ctx: ParserRuleContext?
) : RHSNode {
    override var type: Type = VoidType


    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>,
        issues: MutableList<Issue>
    ) {

        firstElem.validate(st, funTable, issues)
        secondElem.validate(st, funTable, issues)

        type = PairType(firstElem.type, secondElem.type, ctx)
    }

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitNewPair(this)
    }


}