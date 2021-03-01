package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.expr.ExprNode
import analyser.nodes.function.FuncNode
import analyser.nodes.type.GenericPair
import exceptions.SemanticsException
import generator.instructions.Instruction
import generator.instructions.branch.BLInstr
import generator.translator.TranslatorContext
import generator.translator.lib.FreePair
import org.antlr.v4.runtime.ParserRuleContext

data class FreeNode(
    val value: ExprNode,
    override val ctx: ParserRuleContext?,
) : StatNode {
    override lateinit var st: SymbolTable


    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.st = st
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