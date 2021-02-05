package analyser.nodes.expr

import analyser.SymbolTable
import analyser.nodes.type.ArrayType
import analyser.nodes.type.Type
import analyser.nodes.type.VoidType
import exceptions.SemanticsException


data class ArrayLiteral(
    val values: List<ExprNode>
) : ExprNode {
    var elemType: Type = VoidType
    override var type: Type = ArrayType(elemType)

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        if (values.isNotEmpty()) {
            values[0].validate(st, funTable)
            elemType = values[0].type
            type = ArrayType(elemType)
        }

        values.forEach {
            it.validate(st, funTable)
            if (it.type != elemType)
                throw SemanticsException("Array elements are of different type: $this")
        }
    }
}