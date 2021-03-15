package tree.nodes.expr

import shell.MemoryTable
import tree.ASTVisitor
import tree.SymbolTable
import tree.nodes.function.FuncNode
import tree.type.EmptyPair
import tree.type.Type

object PairLiteral : Literal {

    override var type: Type = EmptyPair

    override fun literalToString(mt: MemoryTable?): String = "(nil)"

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

class PairMemoryLiteral(
    var firstLiteral: Literal,
    var secondLiteral: Literal,
    override var type: Type
) : Literal {
    //Spec says to print the hex address but kotlin doesn't allow you to
    override fun literalToString(mt: MemoryTable?): String =
        "<${firstLiteral.literalToString(mt)},${secondLiteral.literalToString(mt)}>"

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
    }

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitPairMemoryLiteral(this)
    }
}