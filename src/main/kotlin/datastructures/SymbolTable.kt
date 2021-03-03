package datastructures

import datastructures.type.Type

class SymbolTable(private val parent: SymbolTable?, val isParamListST: Boolean = false) {

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
        val offset = if (isParamListST) 4 else 0

        if (id in isDeclared) {
            return totalVarSize - map[id]!!.second + offset
        }
        return parent!!.getVariablePosition(id) + totalVarSize
    }

    fun varSizeTotal(): Int {
        var curScope = this
        var size = 0

        while (curScope.parent != null) {
            if (!curScope.isParamListST)
                size += curScope.totalVarSize
            curScope = curScope.parent!!
        }
        return size
    }
}
