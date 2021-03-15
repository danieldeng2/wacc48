package tree.nodes

import tree.ASTVisitor
import tree.SymbolTable
import tree.nodes.function.FuncNode

interface ASTNode {

    fun validate(st: SymbolTable, funTable: MutableMap<String, FuncNode>)

    fun acceptVisitor(visitor: ASTVisitor)
}
