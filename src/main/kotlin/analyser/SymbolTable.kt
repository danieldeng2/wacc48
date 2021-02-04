package analyser

import analyser.nodes.ASTNode

class SymbolTable(private val parent: SymbolTable?) {

    private val map: MutableMap<String, ASTNode> = HashMap()

    /**
     * @return whether SymbolTable contains the [key]
     */
    operator fun contains(key: String): Boolean =
        key in map || parent?.contains(key) ?: false

    /**
     * @return whether current or ancestor SymbolTable contains [key]
     */
    fun containsInCurrentScope(key: String): Boolean =
        key in map

    /**
     * Look-up [key] in all scopes
     * @return ASTNode associated to [key] if found, null otherwise
     */
    operator fun get(key: String): ASTNode? {
        if (key in map) {
            return map[key]
        }
        return parent?.get(key)
    }

    fun add(id: String, node: ASTNode) {
        map[id] = node
    }

}
