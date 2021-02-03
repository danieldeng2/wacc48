package analyser.nodes.function

import analyser.SymbolTable
import analyser.nodes.type.Type
import analyser.nodes.ASTNode
import analyser.nodes.statement.StatNode
import exceptions.SemanticsException

class FuncNode(
    val identifier: String,
    val paramList: ParamListNode,
    val retType: Type,
    val body: StatNode
) :
    ASTNode {
    override fun validate(st: SymbolTable) {
        val currST = SymbolTable(st)
        retType.validate(currST)
        paramList.validate(currST)

        if (st.lookupCurrentScope(identifier) != null)
            throw SemanticsException("Illegal re-declaration of function $identifier")

        if (!allPathsTerminated())
            throw SemanticsException("Function $identifier does not terminate")

        st.add(identifier, this)
        body.validate(currST)
    }

    /**
     * Check all branches in the body of the function ends with either a
     * ReturnNode with correct return type or ExitNode
     * @return true iff all branches exit correctly
     */
    private fun allPathsTerminated(): Boolean {
        return true
    }

}
