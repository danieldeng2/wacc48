package datastructures.nodes.statement

import datastructures.SymbolTable
import datastructures.nodes.expr.ExprNode
import datastructures.nodes.function.FuncNode
import datastructures.nodes.type.GenericPair
import analyser.exceptions.SemanticsException
import generator.instructions.Instruction
import generator.instructions.branch.BLInstr
import generator.translator.TranslatorContext
import generator.translator.lib.FreePair
import org.antlr.v4.runtime.ParserRuleContext

data class FreeNode(
    val value: ExprNode,
    val ctx: ParserRuleContext?,
) : StatNode {

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        value.validate(st, funTable)

        if (value.type !is GenericPair)
            throw SemanticsException("Cannot free ${value.type}", ctx)
    }

    override fun translate(ctx: TranslatorContext) =
        mutableListOf<Instruction>().apply {
            ctx.addLibraryFunction(FreePair)

            addAll(value.translate(ctx))
            add(BLInstr(FreePair.label))
        }
}