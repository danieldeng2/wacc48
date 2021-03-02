package datastructures.nodes.statement

import datastructures.SymbolTable
import datastructures.nodes.expr.ExprNode
import datastructures.nodes.function.FuncNode
import datastructures.type.IntType
import analyser.exceptions.SemanticsException
import generator.instructions.Instruction
import generator.instructions.branch.BLInstr
import generator.translator.TranslatorContext
import org.antlr.v4.runtime.ParserRuleContext

data class ExitNode(
    val value: ExprNode,
    val ctx: ParserRuleContext?
) : StatNode {


    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {

        value.validate(st, funTable)
        if (value.type != IntType)
            throw SemanticsException(
                "Exit must take integer as input, got ${value.type} instead",
                ctx
            )
    }

    override fun translate(ctx: TranslatorContext) =
        mutableListOf<Instruction>().apply {
            addAll(value.translate(ctx))
            add(BLInstr("exit"))
        }

}
