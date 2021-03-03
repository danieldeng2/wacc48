package tree.nodes.statement

import tree.SymbolTable
import tree.nodes.function.FuncNode
import generator.translator.CodeGeneratorVisitor

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
}
