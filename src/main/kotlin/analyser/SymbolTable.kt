package analyser

import analyser.nodes.ASTNode
import analyser.nodes.expr.BoolLiteral
import analyser.nodes.expr.CharLiteral
import analyser.nodes.expr.IntLiteral
import analyser.nodes.expr.StringLiteral

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

    fun getLocalVariablesSize(): Int {
        var size = 0
        for ((_, v) in map) {
            when (v) {
                is IntLiteral -> size += 4
                is CharLiteral -> size += 4
                is StringLiteral -> size += 4
                is BoolLiteral -> size += 1
            }
        }
        return size
    }
}
