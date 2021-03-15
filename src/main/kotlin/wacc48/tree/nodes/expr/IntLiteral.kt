package wacc48.tree.nodes.expr

import wacc48.analyser.exceptions.SemanticsException
import org.antlr.v4.runtime.ParserRuleContext
import wacc48.shell.MemoryTable
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.function.FuncNode
import wacc48.tree.type.IntType
import wacc48.tree.type.Type

data class IntLiteral(
    var value: Int,
    val ctx: ParserRuleContext?
) : Literal {

    override var type: Type = IntType

    override fun literalToString(mt: MemoryTable?): String = value.toString()

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {

        if (value > IntType.max || value < IntType.min)
            throw SemanticsException("IntLiteral $value is out of range", ctx)
    }

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitIntLiteral(this)
    }
}