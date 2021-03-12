package tree.nodes.statement

import analyser.exceptions.SemanticsException
import tree.SymbolTable
import tree.nodes.expr.ExprNode
import tree.nodes.function.FuncNode
import tree.type.IntType
import generator.translator.CodeGeneratorVisitor
import org.antlr.v4.runtime.ParserRuleContext
import shell.CodeEvaluatorVisitor
import tree.nodes.expr.Literal

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

    override fun acceptCodeEvalVisitor(visitor: CodeEvaluatorVisitor): Literal? {
        return visitor.translateExit(this)
    }

}
