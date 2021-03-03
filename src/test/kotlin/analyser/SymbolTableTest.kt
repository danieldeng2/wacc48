package analyser

import datastructures.SymbolTable
import datastructures.nodes.expr.BoolLiteral
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SymbolTableTest {
    private val symbolTable = SymbolTable(null)

    @Test
    fun setedNodeIsReturnedByGet() {
        val node = BoolLiteral(true, null)
        symbolTable["Boolean True"] = node.type

        assertEquals(symbolTable["Boolean True"], node.type)
    }

    @Test
    fun getReturnsNullForUnknownKey() {
        assertNull(symbolTable["Boolean True"])
    }

    @Test
    fun containsCurrentScopeReturnsTrueForsetedKey() {
        val node = BoolLiteral(true, null)
        symbolTable["Boolean True"] = node.type
        assertTrue(symbolTable.containsInCurrentScope("Boolean True"))
    }

    @Test
    fun containsCurrentScopeReturnsFalseForUnknownKey() {
        assertFalse(symbolTable.containsInCurrentScope("Boolean True"))
    }

    @Test
    fun containsAnyScopeReturnsTrueForKeyInParent() {
        val childTable = SymbolTable(symbolTable, true)
        val node = BoolLiteral(true, null)
        symbolTable["Boolean True"] = node.type

        assertTrue(childTable.containsInAnyScope("Boolean True"))
    }

    @Test
    fun containsCurrentScopeReturnsFalseForKeyInParent() {
        val childTable = SymbolTable(symbolTable, true)
        val node = BoolLiteral(true, null)
        symbolTable["Boolean True"] = node.type

        assertFalse(childTable.containsInCurrentScope("Boolean True"))
    }
}