package wacc48.tree.nodes.assignment

import org.antlr.v4.runtime.ParserRuleContext
import wacc48.analyser.exceptions.Issue
import wacc48.analyser.exceptions.addSemantic
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.expr.ExprNode
import wacc48.tree.nodes.function.FuncNode
import wacc48.tree.type.PairType
import wacc48.tree.type.Type
import wacc48.tree.type.VoidType

data class PairElemNode(
    var expr: ExprNode,
    val isFirst: Boolean,
    val ctx: ParserRuleContext?
) : LHSNode, RHSNode {
    override var type: Type = VoidType
    lateinit var st: SymbolTable
    override var mode: AccessMode = AccessMode.READ

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>,
        issues: MutableList<Issue>
    ) {
        this.st = st
        expr.validate(st, funTable, issues)

        if (expr.type !is PairType) {
            issues.addSemantic("Cannot dereference pair", ctx)
            return
        }

        val nameType = expr.type
        if (nameType is PairType) {
            type = when {
                isFirst -> nameType.firstType
                else -> nameType.secondType
            }
        }
    }

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitPairElem(this)
    }


}