package tree.nodes.expr

import org.antlr.v4.runtime.ParserRuleContext
import shell.MemoryTable
import tree.ASTVisitor
import tree.SymbolTable
import tree.nodes.function.FuncNode
import tree.type.CharType
import tree.type.Type

data class CharLiteral(val value: Char, val ctx: ParserRuleContext?) : Literal {
    override var type: Type = CharType

    override fun literalToString(mt: MemoryTable?): String = value.toString()

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
    }

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitCharLiteral(this)
    }
}

