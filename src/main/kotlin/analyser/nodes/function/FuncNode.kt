package analyser.nodes.function

import analyser.SymbolTable
import analyser.nodes.type.Type
import analyser.nodes.ASTNode
import analyser.nodes.statement.StatNode

class FuncNode(val identifier: String, val paramList: ParamListNode) : ASTNode {
    var retType: Type? = null
    var statNode: StatNode? = null

    override fun isValid(st: SymbolTable): Boolean {
        if (st.lookupOuterScopes(identifier) != null) {
            //TODO: Error
            println("Illegal re-declaration of $identifier")
            return false
        }

        if (allPathsTerminated()) {
            st.add(identifier, this)
            return true
        }
        return false
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
