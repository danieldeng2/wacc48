package analyser

import analyser.nodes.type.Type

class SymbolTable(private val parent: SymbolTable?) {

    private val map: MutableMap<String, Pair<Type, Int>> = HashMap()
    private val isDeclared: MutableSet<String> = HashSet()
    var totalVarSize = 0
        private set

    fun containsInCurrentScope(key: String): Boolean = key in map

    fun containsInAnyScope(key: String): Boolean =
        key in map || parent?.containsInAnyScope(key) ?: false


    operator fun get(key: String): Type? {
        if (key in map) {
            return map[key]?.first
        }
        return parent?.get(key)
    }

    operator fun set(id: String, type: Type) {
        totalVarSize += type.reserveStackSize
        map[id] = Pair(type, totalVarSize)
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
}
