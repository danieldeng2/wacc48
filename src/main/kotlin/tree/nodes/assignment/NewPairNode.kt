package tree.nodes.assignment

import org.antlr.v4.runtime.ParserRuleContext
import tree.ASTVisitor
import tree.SymbolTable
import tree.nodes.expr.ExprNode
import tree.nodes.function.FuncNode
import tree.type.PairType
import tree.type.Type
import tree.type.VoidType

data class NewPairNode(
    var firstElem: ExprNode,
    var secondElem: ExprNode,
    val ctx: ParserRuleContext?
) : RHSNode {
    override var type: Type = VoidType


    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {

        firstElem.validate(st, funTable)
        secondElem.validate(st, funTable)

        type = PairType(firstElem.type, secondElem.type, ctx)
    }

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitNewPair(this)
    }


}