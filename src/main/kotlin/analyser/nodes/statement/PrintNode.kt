package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.assignment.AccessMode
import analyser.nodes.expr.ArrayElement
import analyser.nodes.expr.ExprNode
import analyser.nodes.type.*
import generator.translator.TranslatorContext
import generator.instructions.*
import generator.instructions.branch.BLInstr
import generator.translator.lib.LibraryFunction
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
            if (value.type == CharType) {
                add(BLInstr("putchar"))
            } else {

                val printFunc = when {
                    value.type == ArrayType(CharType, null) -> getPrintOption(StringType)
                    value is ArrayElement ->  {

                        if (value.type is ArrayType && (value.type as ArrayType).elementType == CharType)
                            getPrintOption(StringType)
                        else
                            getPrintOption(value.baseType)
                    }

                    else -> getPrintOption(value.type)
                }

                ctx.addLibraryFunction(printFunc)
                add(BLInstr(printFunc.label))
            }

            if (returnAfterPrint) {
                ctx.addLibraryFunction(PrintLn)
                add(BLInstr(PrintLn.label))
            }
        }

    private fun getPrintOption(exprType: Type) =
        when (exprType) {
            IntType -> PrintInt
            StringType -> PrintStr
            BoolType -> PrintBool
            is GenericPair, is ArrayType -> PrintReference
            else -> throw UnexpectedException(
                "Else branch should not be reached for operator ${value.type}"
            )
        }
}