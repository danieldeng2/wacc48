package analyser.nodes.expr

import analyser.SymbolTable
import analyser.nodes.assignment.LHSNode
import analyser.nodes.function.ParamNode
import analyser.nodes.type.ArrayType
import analyser.nodes.type.Type
import analyser.nodes.type.VoidType
import exceptions.SemanticsException

data class ArrayElement(
    val name: String,
    val indices: List<ExprNode>
) : ExprNode, LHSNode {
    override var type: Type = VoidType
    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        if (!st.containsInAnyScope(name))
            throw SemanticsException("Cannot find array $name")
        indices.forEach { it.validate(st, funTable) }
        val typedElem = st[name] as ParamNode
        val arrayType = typedElem.type
        if (arrayType !is ArrayType)
            throw SemanticsException("$name is not an array")

        type = arrayType.elementType
    }
}