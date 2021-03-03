package datastructures.nodes.expr

import analyser.exceptions.SemanticsException
import datastructures.SymbolTable
import datastructures.nodes.function.FuncNode
import datastructures.type.IntType
import datastructures.type.Type
import generator.translator.CodeGeneratorVisitor
import org.antlr.v4.runtime.ParserRuleContext

data class IntLiteral(
    val value: Int,
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

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateIntLiteral(this)
    }
}