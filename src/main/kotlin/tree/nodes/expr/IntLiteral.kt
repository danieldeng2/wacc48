package tree.nodes.expr

import analyser.exceptions.SemanticsException
import tree.SymbolTable
import tree.nodes.function.FuncNode
import tree.type.IntType
import tree.type.Type
import org.antlr.v4.runtime.ParserRuleContext
import tree.ASTVisitor

data class IntLiteral(
    var value: Int,
    val ctx: ParserRuleContext?
) : ExprNode {
    override var type: Type = IntType

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