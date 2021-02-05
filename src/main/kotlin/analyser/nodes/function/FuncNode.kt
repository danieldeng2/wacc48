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
) :
    ASTNode {

    private var hasValuatedPrototype = false

    fun validatePrototype(ft: SymbolTable) {
        hasValuatedPrototype = true

        if (ft.containsInCurrentScope(identifier))
            throw SemanticsException("Illegal re-declaration of function $identifier")

        ft.add(identifier, this)
    }

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        if (!hasValuatedPrototype)
            validatePrototype(funTable)

        val paramST = SymbolTable(st)

        retType.validate(st, funTable)
        paramList.validate(paramST, funTable)
        body.validate(SymbolTable(paramST), funTable)

        if (!allPathsTerminated(body))
            throw SyntaxException("Function $identifier must end with either a return or exit")
    }

    private fun allPathsTerminated(body: StatNode): Boolean {
        var lastStat = body
        while (lastStat is SeqNode) {
            lastStat = lastStat.secondStat
        }

        if (lastStat is ReturnNode && lastStat.value.type != retType)
            throw SemanticsException("Function $identifier must return $retType")

        return when (lastStat) {
            is ReturnNode -> true
            is ExitNode -> true
            is BeginNode -> allPathsTerminated(lastStat.stat)
            is IfNode -> allPathsTerminated(lastStat.trueStat)
                    && allPathsTerminated(lastStat.falseStat)
            else -> false
        }
    }

}
