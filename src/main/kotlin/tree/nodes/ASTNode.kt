package tree.nodes

import tree.SymbolTable
import tree.nodes.function.FuncNode
import generator.translator.CodeGeneratorVisitor

interface ASTNode {

    fun validate(st: SymbolTable, funTable: MutableMap<String, FuncNode>)

    fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor)
}
