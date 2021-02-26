package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.expr.ExprNode
import analyser.nodes.type.*
import generator.translator.TranslatorContext
import generator.instructions.*
import generator.translator.lib.print.*
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

            val printFunc = when (value.type) {
                IntType -> PrintInt
                StringType -> PrintStr
                BoolType -> PrintBool
                else -> TODO("Implement ${value.type} print options")
            }

            ctx.addPrintFunc(printFunc)
            add(BLInstr(printFunc.label))

            if (returnAfterPrint) {
                ctx.addPrintFunc(PrintLn)
                add(BLInstr(PrintLn.label))
            }
        }
}