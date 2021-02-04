package analyser.nodes.expr

import analyser.SymbolTable
import analyser.nodes.type.Type
import analyser.nodes.type.VoidType
import exceptions.SemanticsException

data class ArrayElement(
    val name: String,
    val indices: List<ExprNode>
) : ExprNode {
    override var type: Type = VoidType
    override fun validate(st: SymbolTable) {
        if (name !in st)
            throw SemanticsException("Cannot find array $name")
        indices.forEach { it.validate(st) }
        val typedElem = st[name] as ArrayLiteral
        type = typedElem.elemType
    }
}