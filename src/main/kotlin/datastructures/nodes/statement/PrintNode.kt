package datastructures.nodes.statement

import datastructures.SymbolTable
import datastructures.nodes.expr.ArrayElement
import datastructures.nodes.expr.ExprNode
import datastructures.nodes.function.FuncNode
import datastructures.type.*
import generator.instructions.Instruction
import generator.instructions.branch.BLInstr
import generator.translator.CodeGeneratorVisitor
import generator.translator.TranslatorContext
import generator.translator.lib.print.*
import org.antlr.v4.runtime.ParserRuleContext
import java.rmi.UnexpectedException

data class PrintNode(
    val value: ExprNode,
    val returnAfterPrint: Boolean = false,
    val ctx: ParserRuleContext?,
) : StatNode {


    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {

        value.validate(st, funTable)
    }

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translatePrint(this)
    }

}