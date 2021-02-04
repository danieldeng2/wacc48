package analyser.nodes.function

import analyser.SymbolTable
import analyser.nodes.type.Type
import analyser.nodes.ASTNode
import analyser.nodes.statement.StatNode
import exceptions.SemanticsException

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

        if (identifier in st)
            throw SemanticsException("Illegal re-declaration of function $identifier")

        st.add(identifier, this)
    }

    override fun validate(st: SymbolTable) {
        if (!hasValuatedPrototype)
            validatePrototype(st)

        val paramScopeST = SymbolTable(st)
        val bodyScopeST = SymbolTable(paramScopeST)

        retType.validate(st)
        paramList.validate(paramScopeST)
        body.validate(bodyScopeST)

        if (!allPathsTerminated())
            throw SemanticsException("Function $identifier does not terminate")

    }

    /**
     * Check all branches in the body of the function ends with either a
     * ReturnNode with correct return type or ExitNode
     * @return true iff all branches exit correctly
     */
    private fun allPathsTerminated(): Boolean {
        // TODO("Not yet implemented")
        return true
    }

}
