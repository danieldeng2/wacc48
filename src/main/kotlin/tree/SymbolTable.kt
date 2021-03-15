package tree

import tree.type.Type


class SymbolTable(
    private val parent: SymbolTable?,
    val isParamListST: Boolean = false
) {
    private var map: MutableMap<String, Pair<Type, Int>> = HashMap()
    private var isDeclared: MutableSet<String> = HashSet()

    var totalVarSize = 0
        private set

    fun clone(): SymbolTable {
        val st = SymbolTable(null)
        st.map = map.toMutableMap()
        st.isDeclared = isDeclared.toMutableSet()
        return st
    }

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

    fun getVariablePosition(id: String): Pair<Int, Boolean> {
        if (id in isDeclared) {
            return Pair(totalVarSize - map[id]!!.second, isParamListST)
        }

        val (parentOffset, isArgument) = parent!!.getVariablePosition(id)

        return Pair(parentOffset + totalVarSize, isArgument)
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
