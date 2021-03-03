package datastructures.nodes.function

import analyser.exceptions.SemanticsException
import datastructures.SymbolTable
import datastructures.nodes.ASTNode
import datastructures.nodes.statement.*
import generator.translator.CodeGeneratorVisitor
import org.antlr.v4.runtime.ParserRuleContext

class MainNode(
    val body: StatNode,
    val ctx: ParserRuleContext?
) : ASTNode {
    lateinit var st: SymbolTable

    override fun validate(st: SymbolTable, funTable: MutableMap<String, FuncNode>) {
        this.st = st
        body.validate(st, funTable)

        if (hasGlobalReturn(body))
            throw SemanticsException("Cannot return in global context", ctx)
    }

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateMain(this)
    }

    private fun hasGlobalReturn(body: StatNode): Boolean =
        when (body) {
            is IfNode -> hasGlobalReturn(body.trueStat) || hasGlobalReturn(body.falseStat)
            is SeqNode -> body.any { hasGlobalReturn(it) }
            is WhileNode -> hasGlobalReturn(body.body)
            is BeginNode -> hasGlobalReturn(body.stat)
            is ReturnNode -> true
            else -> false
        }

}