package generator.translator.helpers

import generator.translator.lib.print.PrintBool
import generator.translator.lib.print.PrintInt
import generator.translator.lib.print.PrintReference
import generator.translator.lib.print.PrintStr
import tree.type.*
import java.rmi.UnexpectedException

/** Selects correct print object based on type of expression needed
 * to be print [exprType]. */
fun getPrintOption(exprType: Type) =
    when (exprType) {
        IntType -> PrintInt
        StringType -> PrintStr
        BoolType -> PrintBool
        is GenericPair, is ArrayType -> PrintReference
        else -> throw UnexpectedException(
            "Else branch should not be reached for operator $exprType"
        )
    }
