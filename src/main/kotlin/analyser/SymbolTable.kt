package analyser

import analyser.nodes.ASTNode
import analyser.nodes.type.*

class SymbolTable(private val parent: SymbolTable?) {

    private val map: MutableMap<String, Pair<ASTNode, Int>> = HashMap()
    private var localStackSize = 0

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
            return map[key]?.first
        }
        return parent?.get(key)
    }

    fun add(id: String, node: ASTNode) {
        localStackSize += getSizeOfVar(node)
        map[id] = Pair(node, localStackSize)
    }

    override fun toString(): String {
        return map.toString()
    }

    fun getLocalVariablesSize() = localStackSize

    private fun getSizeOfVar(node: ASTNode): Int =
        when (node) {
            !is Typable -> 0
            else -> node.type.reserveStackSize
        }

    fun getOffsetOfVar(id: String): Int {
        var curST = this
        var offset = 0

        while (!curST.map.containsKey(id)) {
            offset += curST.localStackSize
            curST = curST.parent!!
        }

        offset += curST.localStackSize - curST.map[id]!!.second
        return offset
    }
}
