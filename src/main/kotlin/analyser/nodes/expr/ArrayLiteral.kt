package analyser.nodes.expr

import analyser.SymbolTable
import analyser.nodes.type.ArrayType
import analyser.nodes.type.Type
import analyser.nodes.type.VoidType
import exceptions.SemanticsException
import org.antlr.v4.runtime.ParserRuleContext


data class ArrayLiteral(
    val values: List<ExprNode>,
    override val ctx: ParserRuleContext?
) : ExprNode {
    var elemType: Type = VoidType
    override var type: Type = ArrayType(elemType, ctx)
    override lateinit var st: SymbolTable
    override lateinit var funTable: SymbolTable

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        this.st = st
        this.funTable = funTable
        if (values.isNotEmpty()) {
            values[0].validate(st, funTable)
            elemType = values[0].type
            type = ArrayType(elemType, ctx)
        }

        values.forEach {
            it.validate(st, funTable)
            if (it.type != elemType)
                throw SemanticsException("Array elements are of different type: $this", ctx)
        }
    }
}