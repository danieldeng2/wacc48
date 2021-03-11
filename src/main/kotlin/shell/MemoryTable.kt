package shell

import analyser.exceptions.SemanticsException
import tree.nodes.expr.Literal
import tree.type.Type

class MemoryTable(private val parent: MemoryTable?) {
    private val map: MutableMap<String, Pair<Type, Literal?>> = HashMap()
    private val isDeclared: MutableSet<String> = HashSet()

    operator fun set(id: String, type: Type, literal: Literal) {
        if (literal.type != type)
            throw SemanticsException(
                "Setting mismatching type(${map[id]?.first}) and literal(${literal.type}) in memory table", null
            )
        map[id] = Pair(type, literal)
        isDeclared.add(id)
    }

    operator fun set(id: String, type: Type) {
        if (map[id] != null)
            throw SemanticsException("Setting new type for already existing id in memory table", null)
        map[id] = Pair(type, null)
    }

    operator fun set(id: String, literal: Literal) {
        if (id in map) {
            if (map[id]?.first != literal.type)
                throw SemanticsException(
                    "Setting mismatching type(${map[id]?.first}) and literal(${literal.type}) in memory table", null
                )
            map[id] = Pair(literal.type, literal)
            isDeclared.add(id)
            return
        }
        parent?.set(id, literal)
    }

    operator fun get(key: String): Pair<Type, Literal?>? {
        if (key in map) {
            return map[key]
        }
        return parent?.get(key)
    }

    fun remove(key: String) {
        if (key in map) {
            map.remove(key)
        }
        parent?.remove(key)
    }

    fun getType(key: String): Type? {
        if (key in map) {
            return map[key]?.first
        }
        return parent?.getType(key)
    }

    fun getLiteral(key: String): Literal? {
        if (key in map) {
            return map[key]?.second
        }
        return parent?.getLiteral(key)
    }

    fun declareVariable(id: String): Boolean {
        if (id in map) {
            isDeclared.add(id)
            return true
        }
        return false
    }
}