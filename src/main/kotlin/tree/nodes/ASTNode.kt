package tree.nodes

import tree.SymbolTable
import tree.nodes.function.FuncNode
import generator.translator.CodeGeneratorVisitor
import shell.CodeEvaluatorVisitor
import tree.nodes.expr.Literal

interface ASTNode {

    fun validate(st: SymbolTable, funTable: MutableMap<String, FuncNode>)

    fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor)
    fun acceptCodeEvalVisitor(visitor: CodeEvaluatorVisitor)
}
