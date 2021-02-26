package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.expr.ExprNode
import analyser.nodes.type.*
import generator.translator.TranslatorContext
import generator.instructions.*
import generator.instructions.branch.BLInstr
import generator.translator.lib.print.*
import org.antlr.v4.runtime.ParserRuleContext
import java.rmi.UnexpectedException

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
                IntType, StringType, BoolType, is GenericPair -> {
                    val printFunc = when (value.type) {
                        IntType -> PrintInt
                        StringType -> PrintStr
                        BoolType -> PrintBool
                        is GenericPair -> PrintPair
                        else -> throw UnexpectedException(
                            "Else branch should not be reached for operator ${value.type}"
                        )
                    }
                    ctx.addLibraryFunction(printFunc)
                    add(BLInstr(printFunc.label))
                }

                CharType -> add(BLInstr("putchar"))
            }

            if (returnAfterPrint) {
                ctx.addLibraryFunction(PrintLn)
                add(BLInstr(PrintLn.label))
            }
        }
}