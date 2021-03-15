package tree.nodes.expr

import tree.SymbolTable
import tree.nodes.function.FuncNode
import tree.type.EmptyPair
import tree.type.Type
import tree.ASTVisitor

object PairLiteral : ExprNode {
    override var type: Type = EmptyPair

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
    }

    override fun toString(): String {
        return "Null"
    }

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitPairLiteral(this)
    }
}
