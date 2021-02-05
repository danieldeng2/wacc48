package analyser

import analyser.nodes.ASTNode

class SymbolTable(private val parent: SymbolTable?) {

    private val map: MutableMap<String, ASTNode> = HashMap()

    /**
     * @return whether SymbolTable contains the [key]
     */

    fun containsInCurrentScope(key: String): Boolean = key in map

    /**
     * @return whether current or ancestor SymbolTable contains [key]
     */
    fun containsInAnyScope(key: String): Boolean =
        key in map || parent?.containsInAnyScope(key) ?: false

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

    override fun toString(): String {
        return map.toString()
    }
}
