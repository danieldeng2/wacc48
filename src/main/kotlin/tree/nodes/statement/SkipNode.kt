package tree.nodes.statement

import tree.SymbolTable
import tree.nodes.function.FuncNode
import generator.translator.CodeGeneratorVisitor
import shell.CodeEvaluatorVisitor
import tree.nodes.expr.Literal

object SkipNode : StatNode {

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
    }

    override fun toString(): String {
        return "Skip"
    }

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {}
    override fun acceptCodeEvalVisitor(visitor: CodeEvaluatorVisitor) {}
}
