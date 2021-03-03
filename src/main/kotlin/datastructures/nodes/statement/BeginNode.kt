package datastructures.nodes.statement

import datastructures.SymbolTable
import datastructures.nodes.function.FuncNode
import generator.translator.CodeGeneratorVisitor
import org.antlr.v4.runtime.ParserRuleContext

data class BeginNode(
    val stat: StatNode,
    val ctx: ParserRuleContext?,
) : StatNode {
    lateinit var currST: SymbolTable

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.currST = SymbolTable(st)
        stat.validate(currST, funTable)
    }

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateBegin(this)
    }
}