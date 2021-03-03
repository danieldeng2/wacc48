package datastructures.nodes.statement

import analyser.exceptions.SemanticsException
import datastructures.SymbolTable
import datastructures.nodes.expr.ExprNode
import datastructures.nodes.function.FuncNode
import datastructures.type.IntType
import generator.translator.CodeGeneratorVisitor
import org.antlr.v4.runtime.ParserRuleContext

data class ExitNode(
    val expr: ExprNode,
    val ctx: ParserRuleContext?
) : StatNode {

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {

        expr.validate(st, funTable)
        if (expr.type != IntType)
            throw SemanticsException(
                "Exit must take integer as input, got ${expr.type} instead",
                ctx
            )
    }

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateExit(this)
    }

}
