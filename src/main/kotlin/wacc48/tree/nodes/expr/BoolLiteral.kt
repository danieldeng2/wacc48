package wacc48.tree.nodes.expr

import org.antlr.v4.runtime.ParserRuleContext
import wacc48.analyser.exceptions.Issue
import wacc48.shell.MemoryTable
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.function.FuncNode
import wacc48.tree.type.BoolType
import wacc48.tree.type.Type

data class BoolLiteral(
    var value: Boolean,
    val ctx: ParserRuleContext?
) : BaseLiteral {

    override var type: Type = BoolType

    override fun literalToString(mt: MemoryTable?): String = value.toString()

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>,
        issues: MutableList<Issue>
    ) {
    }

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitBoolLiteral(this)
    }

}