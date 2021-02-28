package analyser

import analyser.nodes.ASTNode
import analyser.nodes.type.Typable

class SymbolTable(private val parent: SymbolTable?) {

    private val map: MutableMap<String, Pair<ASTNode, Int>> = HashMap()
    private val isDeclared: MutableSet<String> = HashSet()
    var totalVarSize = 0
        private set

    fun containsInCurrentScope(key: String): Boolean = key in map

    fun containsInAnyScope(key: String): Boolean =
        key in map || parent?.containsInAnyScope(key) ?: false


    operator fun get(key: String): ASTNode? {
        if (key in map) {
            return map[key]?.first
        }
        return parent?.get(key)
    }

    fun add(id: String, node: ASTNode) {
        totalVarSize += node.size()
        map[id] = Pair(node, totalVarSize)
    }

    fun declareVariable(id: String): Boolean {
        if (id in map) {
            isDeclared.add(id)
            return true
        }
        return false
    }

    fun getVariablePosition(id: String): Int {
        if (id in isDeclared) {
            return totalVarSize - map[id]!!.second
        }
        return parent!!.getVariablePosition(id) + totalVarSize
    }

    private fun ASTNode.size() =
        when (this) {
            is Typable -> type.reserveStackSize
            else -> 0
        }
}
