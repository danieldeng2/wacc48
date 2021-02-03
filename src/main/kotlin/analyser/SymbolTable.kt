package analyser

import analyser.nodes.ASTNode

class SymbolTable(private val parent: SymbolTable?) {

    private val map: MutableMap<String, ASTNode> = HashMap()

    /**
     * Only a global scope should have a 'null' parent since this is a tree
     * structure
     * @return true iff current symbol table is at global scope
     */
    fun isGlobalScope(): Boolean = parent == null

    /**
     * Look-up [key] in the current scope
     * @return ASTNode associated to [key] if found, null otherwise
     */
    fun lookupCurrentScope(key: String): ASTNode? {
        return map[key]
    }

    /**
     * Look-up [key] in current and all ancestor scopes
     * @return ASTNode in innermost scope if [key] is found, null otherwise
     */
    fun lookupOuterScopes(key: String): ASTNode? {
        var st: SymbolTable? = this
        do {
            val node = st!!.lookupCurrentScope(key)
            if (node != null) return node
            st = st.parent
        } while (st != null)
        return null
    }

    fun add(id: String, node: ASTNode) {
        map[id] = node
    }

}
