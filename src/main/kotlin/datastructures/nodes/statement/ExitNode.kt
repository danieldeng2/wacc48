package datastructures.nodes.statement

import datastructures.SymbolTable
import datastructures.nodes.expr.ExprNode
import datastructures.nodes.function.FuncNode
import datastructures.type.IntType
import analyser.exceptions.SemanticsException
import generator.instructions.Instruction
import generator.instructions.branch.BLInstr
import generator.translator.CodeGeneratorVisitor
import generator.translator.TranslatorContext
import org.antlr.v4.runtime.ParserRuleContext

data class ExitNode(
    val expr: ExprNode,
    val ctx: ParserRuleContext?
) : StatNode {

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {

        expr.validate(st, funTable)
        if (expr.type != IntType)
            throw SemanticsException(
                "Exit must take integer as input, got ${expr.type} instead",
                ctx
            )
    }

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateExit(this)
    }

}
