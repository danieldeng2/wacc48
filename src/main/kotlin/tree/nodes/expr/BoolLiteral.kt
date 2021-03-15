package tree.nodes.expr

import tree.SymbolTable
import tree.nodes.function.FuncNode
import tree.type.BoolType
import tree.type.Type
import org.antlr.v4.runtime.ParserRuleContext
import shell.MemoryTable
import tree.ASTVisitor

data class BoolLiteral(
    var value: Boolean,
    val ctx: ParserRuleContext?
) : Literal {

    override var type: Type = BoolType

    override fun literalToString(mt: MemoryTable?): String = value.toString()

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
    }

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitBoolLiteral(this)
    }

}