package datastructures.nodes.statement

import datastructures.SymbolTable
import datastructures.nodes.function.FuncNode
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
