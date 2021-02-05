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

    fun validatePrototype(st: SymbolTable) {
        hasValuatedPrototype = true

        if (st.containsInCurrentScope(identifier))
            throw SemanticsException("Illegal re-declaration of function $identifier")

        st.add(identifier, this)
    }

    override fun validate(st: SymbolTable) {
        if (!hasValuatedPrototype)
            validatePrototype(st)

        if (!allPathsTerminated(body))
            throw SyntaxException("Function $identifier must end with either a return or exit")

        val paramScopeST = SymbolTable(st)
        val bodyScopeST = SymbolTable(paramScopeST)

        retType.validate(st)
        paramList.validate(paramScopeST)
        body.validate(bodyScopeST)

    }

    private fun allPathsTerminated(body: StatNode): Boolean {
        var lastStat = body
        while (lastStat is SeqNode) {
            lastStat = lastStat.secondStat
        }

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
