package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.expr.ExprNode
import analyser.nodes.type.*
import generator.translator.TranslatorContext
import generator.armInstructions.*
import generator.translator.print.PrintBool
import generator.translator.print.PrintInt
import generator.translator.print.PrintLn
import generator.translator.print.PrintStr
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
                IntType -> add(ctx.addPrintFunc(PrintInt))
                StringType -> add(ctx.addPrintFunc(PrintStr))
                BoolType -> add(ctx.addPrintFunc(PrintBool))
                else -> TODO("Implement ${value.type} print options")
            }

            if (returnAfterPrint) add(ctx.addPrintFunc(PrintLn))
        }
}