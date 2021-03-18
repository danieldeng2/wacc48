package wacc48.generator.translator.helpers

import wacc48.generator.translator.lib.print.*
import wacc48.tree.type.*
import java.rmi.UnexpectedException

/** Selects correct print object based on type of expression needed
 * to be print [exprType]. */
fun getPrintOption(exprType: Type) =
    when (exprType) {
        IntType -> PrintInt
        StringType -> PrintStr
        BoolType -> PrintBool
        ArrayType(CharType) -> PrintCharArray
        is GenericPair, is ArrayType -> PrintReference
        else -> throw UnexpectedException(
            "Else branch should not be reached for operator $exprType"
        )
    }
