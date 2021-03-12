package tree.nodes.function

import tree.SymbolTable
import tree.nodes.ASTNode
import tree.nodes.expr.ExprNode
import generator.translator.CodeGeneratorVisitor
import org.antlr.v4.runtime.ParserRuleContext
import shell.CodeEvaluatorVisitor
import tree.nodes.expr.Literal

data class ArgListNode(
    val args: List<ExprNode>,
    val ctx: ParserRuleContext?
) : ASTNode {
    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        args.forEach {
            it.validate(st, funTable)
        }
    }

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateArgList(this)
    }

    override fun acceptCodeEvalVisitor(visitor: CodeEvaluatorVisitor) {
        visitor.translateArgList(this)
    }
}