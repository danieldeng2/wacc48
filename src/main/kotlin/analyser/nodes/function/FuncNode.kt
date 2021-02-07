package analyser.nodes.function

import analyser.SymbolTable
import analyser.nodes.type.Type
import analyser.nodes.ASTNode
import analyser.nodes.statement.*
import exceptions.SemanticsException
import exceptions.SyntaxException

data class FuncNode(
    val identifier: String,
    val paramList: ParamListNode,
    val retType: Type,
    val body: StatNode
) : ASTNode {

    fun validatePrototype(ft: SymbolTable) {
        if (ft.containsInCurrentScope(identifier))
            throw SemanticsException(".*", null)

        ft.add(identifier, this)
    }

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        val paramST = SymbolTable(st)

        retType.validate(st, funTable)
        paramList.validate(paramST, funTable)
        body.validate(SymbolTable(paramST), funTable)

        if (!correctReturnType(body))
            throw SemanticsException(".*", null)
    }

    private fun correctReturnType(body: StatNode): Boolean {
        var lastStat = body
        while (lastStat is SeqNode) {
            lastStat = lastStat.secondStat
        }

        return when (lastStat) {
            is BeginNode -> correctReturnType(lastStat.stat)
            is IfNode -> correctReturnType(lastStat.trueStat)
                    && correctReturnType(lastStat.falseStat)
            is ReturnNode -> lastStat.value.type == retType
            else -> true
        }
    }
}
