package analyser.nodes.assignment

import analyser.SymbolTable
import analyser.nodes.expr.ExprNode
import analyser.nodes.type.PairType
import analyser.nodes.type.Type
import analyser.nodes.type.VoidType
import exceptions.SemanticsException

data class PairElemNode(
    private val name: ExprNode,
    private val isFirst: Boolean
) : LHSNode, RHSNode {
    override var type: Type = VoidType

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        name.validate(st, funTable)

        if (name.type !is PairType)
            throw SemanticsException(".*", null)

        val nameType = name.type
        if (nameType is PairType) {
            type = when {
                isFirst -> nameType.firstType
                else -> nameType.secondType
            }
        }

    }

}