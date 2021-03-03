package tree.nodes.statement

import analyser.exceptions.SemanticsException
import tree.SymbolTable
import tree.nodes.expr.ExprNode
import tree.nodes.function.FuncNode
import tree.type.GenericPair
import generator.translator.CodeGeneratorVisitor
import org.antlr.v4.runtime.ParserRuleContext

data class FreeNode(
    val value: ExprNode,
    val ctx: ParserRuleContext?,
) : StatNode {

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        value.validate(st, funTable)

        if (value.type !is GenericPair)
            throw SemanticsException("Cannot free ${value.type}", ctx)
    }

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateFree(this)
    }
}