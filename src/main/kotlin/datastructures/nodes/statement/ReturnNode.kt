package datastructures.nodes.statement

import datastructures.SymbolTable
import datastructures.nodes.expr.ExprNode
import datastructures.nodes.function.FuncNode
import generator.instructions.Instruction
import generator.instructions.operands.Register
import generator.instructions.stack.POPInstr
import generator.translator.CodeGeneratorVisitor
import generator.translator.TranslatorContext
import generator.translator.helpers.endAllScopes
import org.antlr.v4.runtime.ParserRuleContext

data class ReturnNode(
    val value: ExprNode,
    val ctx: ParserRuleContext?,
) : StatNode {
    lateinit var st: SymbolTable

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.st = st
        value.validate(st, funTable)
    }

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateReturn(this)
    }
}