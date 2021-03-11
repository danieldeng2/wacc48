package tree.nodes.statement

import tree.SymbolTable
import tree.nodes.expr.ExprNode
import tree.nodes.function.FuncNode
import generator.translator.CodeGeneratorVisitor
import org.antlr.v4.runtime.ParserRuleContext
import shell.CodeEvaluatorVisitor
import tree.nodes.expr.Literal

data class PrintNode(
    val value: ExprNode,
    val returnAfterPrint: Boolean = false,
    val ctx: ParserRuleContext?,
) : StatNode {


    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {

        value.validate(st, funTable)
    }

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translatePrint(this)
    }

    override fun acceptCodeEvalVisitor(visitor: CodeEvaluatorVisitor): Literal? {
        return visitor.translatePrint(this)
    }

}