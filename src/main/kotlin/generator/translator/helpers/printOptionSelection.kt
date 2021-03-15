package generator.translator.helpers

import generator.translator.lib.print.*
import tree.type.*
import java.rmi.UnexpectedException

/** Selects correct print object based on type of expression needed
 * to be print [exprType]. */
fun getPrintOption(exprType: Type) =
    when (exprType) {
        IntType -> PrintInt
        StringType -> PrintStr
        BoolType -> PrintBool
        ArrayType(CharType, null) -> PrintCharArray
        is GenericPair, is ArrayType -> PrintReference
        else -> throw UnexpectedException(
            "Else branch should not be reached for operator $exprType"
        )
    }
