package analyser.nodes.assignment

import analyser.SymbolTable
import analyser.nodes.expr.ExprNode
import analyser.nodes.type.PairType
import analyser.nodes.type.Type
import analyser.nodes.type.VoidType
import exceptions.SemanticsException
import org.antlr.v4.runtime.ParserRuleContext

data class PairElemNode(
    private val name: ExprNode,
    private val isFirst: Boolean,
    override val ctx: ParserRuleContext?
) : LHSNode, RHSNode {
    override var type: Type = VoidType
    override lateinit var st: SymbolTable
    override lateinit var funTable: SymbolTable
    override var isDeclaring: Boolean = false

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        this.st = st
        this.funTable = funTable
        name.validate(st, funTable)

        if (name.type !is PairType)
            throw SemanticsException("Cannot dereference pair $name", ctx)

        val nameType = name.type
        if (nameType is PairType) {
            type = when {
                isFirst -> nameType.firstType
                else -> nameType.secondType
            }
        }

    }

}