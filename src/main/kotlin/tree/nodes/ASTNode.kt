package tree.nodes

import tree.SymbolTable
import tree.nodes.function.FuncNode
import tree.ASTVisitor

interface ASTNode {

    fun validate(st: SymbolTable, funTable: MutableMap<String, FuncNode>)

    fun acceptVisitor(visitor: ASTVisitor)
}
