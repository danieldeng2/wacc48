package wacc48.tree.nodes.expr

import wacc48.analyser.exceptions.Issue
import wacc48.shell.MemoryTable
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.ASTNode
import wacc48.tree.nodes.function.FuncNode
import wacc48.tree.type.EmptyPair
import wacc48.tree.type.Type

object PairLiteral : Literal {

    override var type: Type = EmptyPair

    override fun literalToString(mt: MemoryTable?): String = "(nil)"

    override val children: List<ASTNode>
        get() = emptyList()

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>,
        issues: MutableList<Issue>
    ) {
    }

    override fun toString(): String {
        return "Null"
    }

    override fun <T> acceptVisitor(visitor: ASTVisitor<T>): T {
        return visitor.visitPairLiteral(this)
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

    override val children: List<ASTNode>
        get() = listOf(firstLiteral, secondLiteral)

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>,
        issues: MutableList<Issue>
    ) {
    }

    override fun <T> acceptVisitor(visitor: ASTVisitor<T>): T {
        return visitor.visitPairMemoryLiteral(this)
    }
}