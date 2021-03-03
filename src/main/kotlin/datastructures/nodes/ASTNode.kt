package datastructures.nodes

import datastructures.SymbolTable
import datastructures.nodes.function.FuncNode
import generator.instructions.Instruction
import generator.translator.CodeGeneratorVisitor
import generator.translator.TranslatorContext

interface ASTNode {

    fun validate(st: SymbolTable, funTable: MutableMap<String, FuncNode>)

    fun translate(ctx: TranslatorContext): List<Instruction>

    fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor)
}
