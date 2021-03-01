package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.expr.ArrayElement
import analyser.nodes.expr.ExprNode
import analyser.nodes.function.FuncNode
import analyser.nodes.type.*
import generator.instructions.Instruction
import generator.instructions.branch.BLInstr
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

    override fun translate(ctx: TranslatorContext) =
        mutableListOf<Instruction>().apply {
            addAll(value.translate(ctx))
            if (value.type == CharType) {
                add(BLInstr("putchar"))
            } else {

                val printFunc = when {
                    value.type == ArrayType(CharType, null) -> getPrintOption(StringType)
                    value is ArrayElement -> {

                        if (value.type is ArrayType && (value.type as ArrayType).elementType == CharType)
                            getPrintOption(StringType)
                        else
                            getPrintOption(value.type)
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