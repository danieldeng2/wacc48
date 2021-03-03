package generator.translator.helpers

import datastructures.type.*
import generator.translator.lib.print.PrintBool
import generator.translator.lib.print.PrintInt
import generator.translator.lib.print.PrintReference
import generator.translator.lib.print.PrintStr
import java.rmi.UnexpectedException


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
