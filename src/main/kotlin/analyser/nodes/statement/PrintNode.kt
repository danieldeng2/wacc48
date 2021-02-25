package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.expr.ExprNode
import analyser.nodes.type.IntType
import analyser.nodes.type.StringType
import generator.PrintOptions
import generator.TranslatorContext
import generator.armInstructions.BLInstr
import generator.armInstructions.Instruction
import org.antlr.v4.runtime.ParserRuleContext

data class PrintNode(
    val value: ExprNode,
    val returnAfterPrint: Boolean = false,
    override val ctx: ParserRuleContext?,
) : StatNode {
    override lateinit var st: SymbolTable
    override lateinit var funTable: SymbolTable

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        this.st = st
        this.funTable = funTable
        value.validate(st, funTable)
    }

    override fun translate(ctx: TranslatorContext) =
        mutableListOf<Instruction>().apply {
            addAll(value.translate(ctx))
            when (value.type) {
                IntType -> add(ctx.addPrintFunc(PrintOptions.INT))
                StringType -> add(ctx.addPrintFunc(PrintOptions.STRING))
                else -> TODO("Implement other print options")
            }

            if (returnAfterPrint) add(ctx.addPrintFunc(PrintOptions.NEWLINE))
        }
}